/**
 * Created by asisodia on 7/21/2016.
 */

import java.util.ArrayList;

public class QuadTree {
    //image map and four children
    private QuadTreeNode root;
    public int depth;

    public QuadTree() {
        root = new QuadTreeNode(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON);
        root.upper_left = new QuadTreeNode("1", root.UL_LAT, root.UL_LON,
                (root.UL_LAT + root.LR_LAT) / 2, (root.UL_LON + root.LR_LON) / 2);
        root.upper_right = new QuadTreeNode("2", root.UL_LAT, (root.UL_LON + root.LR_LON)/2,
                (root.UL_LAT + root.LR_LAT) / 2, root.LR_LON);
        root.lower_left = new QuadTreeNode("3", (root.UL_LAT + root.LR_LAT)/2, root.UL_LON,
                root.LR_LAT, (root.UL_LON + root.LR_LON) / 2);
        root.lower_right = new QuadTreeNode("4", (root.UL_LAT + root.LR_LAT) / 2, (root.UL_LON + root.LR_LON) / 2,
                root.LR_LAT, root.LR_LON);
    }

//    public int calcDepthPix(double ullon, double lrlon, double w) {
//        depth = 0;
//        double widthTile = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON);
//        while (!((widthTile/MapServer.TILE_SIZE) < (Math.abs(lrlon - ullon)/(w)))) {
//            widthTile = widthTile/(Math.pow(2.0, depth));
//            depth++;
//        }
//        return (depth - 1);
//    }

    public int calcMaxDepth(double ullon, double lrlon, double w) {
        depth = 0;
        double queryBoxDPP = Math.abs(lrlon - ullon)/w;
        double tileDPP = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)/MapServer.TILE_SIZE;
        while (depth <= 6 && tileDPP > queryBoxDPP) {
            depth++;
            tileDPP /=2;
        }
        return depth;
    }

    public ArrayList<QuadTreeNode> traverseHelper(ArrayList<QuadTreeNode> arr, QuadTreeNode node, double ullat, double ullon, double lrlat, double lrlon) {

        //System.out.println("level: " + node.getLevel() + " depth: " + depth);
        if (node.getLevel() >= depth) {
            //System.out.println("node:" + node.imageName);
            arr.add(node);
        } else {
            if (node.upper_left.intersect(ullat, ullon, lrlat, lrlon)) {
                //traverseHelper(arr, node.upper_left, node.upper_left.UL_LAT, node.upper_left.UL_LON);
                traverseHelper(arr, node.upper_left, ullat, ullon, lrlat, lrlon);
            }
            if (node.upper_right.intersect(ullat, ullon, lrlat, lrlon)) {
                traverseHelper(arr, node.upper_right, ullat, ullon, lrlat, lrlon);
            }
            if (node.lower_left.intersect(ullat, ullon, lrlat, lrlon)) {
                traverseHelper(arr, node.lower_left, ullat, ullon, lrlat, lrlon);
            }
            if (node.lower_right.intersect(ullat, ullon, lrlat, lrlon)) {

                traverseHelper(arr, node.lower_right, ullat, ullon, lrlat, lrlon);

            }
        }
        //System.out.println("size: " + arr.size());
        return arr;
    }

    public ArrayList<QuadTreeNode> traverseTree(double ullat, double ullon, double lrlat, double lrlon, double w, double h) {
        ArrayList<QuadTreeNode> arr = new ArrayList();
        depth = calcMaxDepth(ullon, lrlon, w);
        //System.out.println("depth: " + depth);
        return traverseHelper(arr, root, ullat, ullon, lrlat, lrlon);
    }

    public class QuadTreeNode implements Comparable<QuadTreeNode> {
        protected QuadTreeNode upper_left, upper_right;
        protected QuadTreeNode lower_left, lower_right;
        protected double UL_LAT, UL_LON, LR_LAT, LR_LON;
        protected String imageName;

        public QuadTreeNode (double UL_LAT, double UL_LON, double LR_LAT, double LR_LON) {
            imageName = "root";
            this.UL_LAT = UL_LAT; // a
            this.UL_LON = UL_LON; // b
            this.LR_LAT = LR_LAT; // c
            this.LR_LON = LR_LON; // d
        }

        public QuadTreeNode (String imageName, double UL_LAT, double UL_LON, double LR_LAT, double LR_LON) {
            this.imageName = imageName;
            this.UL_LAT = UL_LAT; // a
            this.UL_LON = UL_LON; // b
            this.LR_LAT = LR_LAT; // c
            this.LR_LON = LR_LON; // d

            if (this.getLevel() < 7) {
                upper_left = new QuadTreeNode(imageName + "1", UL_LAT, UL_LON,
                        (UL_LAT + LR_LAT) / 2, (UL_LON + LR_LON) / 2);
                upper_right = new QuadTreeNode(imageName + "2", UL_LAT, (UL_LON + LR_LON)/2,
                        (UL_LAT + LR_LAT) / 2, LR_LON);
                lower_left = new QuadTreeNode(imageName + "3", (UL_LAT + LR_LAT)/2, UL_LON,
                        LR_LAT, (UL_LON + LR_LON) / 2);
                lower_right = new QuadTreeNode(imageName + "4", (UL_LAT + LR_LAT) / 2, (UL_LON + LR_LON) / 2,
                        LR_LAT, LR_LON);

            }
        }

        public int getLevel() {
            if (imageName.equals("root")) {
                return 0;
            }
            return imageName.length();
        }

        public boolean intersect(double user_UL_LAT, double user_UL_LON, double user_LR_LAT, double user_LR_LON) {
            // tile = this.(LR_LAT...)
            //double[] userLAT = {user_UL_LAT, user_UL_LAT,user_LR_LAT, user_LR_LAT};
            //double[] userLON = {user_UL_LON, user_LR_LON,user_UL_LON, user_LR_LON};

            if(this.LR_LON < user_UL_LON) return false;
            if(this.UL_LON > user_LR_LON) return false;
            if(this.LR_LAT > user_UL_LAT) return false;
            if(this.UL_LAT < user_LR_LAT) return false;
            return true;

            /*for (int i = 0; i < 4; i++) {
                if (check_LAT(userLAT[i], this.UL_LAT, this.LR_LAT) &&
                        check_LON(userLON[i], this.UL_LON, this.LR_LON)) {
                    return true;
                }
            }
            if (check_LAT(this.UL_LAT,userLAT[0], userLAT[3]) && check_LON(this.UL_LON,userLON[0], userLON[3])) {
                return true;
            }
            if (check_LAT(this.UL_LAT,userLAT[0], userLAT[3]) && check_LON(this.LR_LON,userLON[0], userLON[3])) {
                return true;
            }
            if (check_LAT(this.UL_LAT,userLAT[0], userLAT[3]) && check_LON(this.LR_LON,userLON[0], userLON[3])) {
                return true;
            }
            if (check_LAT(this.LR_LAT,userLAT[0], userLAT[3]) && check_LON(this.LR_LON,userLON[0], userLON[3])) {
                return true;
            }
            return false; */
            // System.out.println("y,x: " + y + "," + x);
            // System.out.println("UL y,x: " + this.UL_LAT + "," + this.UL_LON);
            // System.out.println("LR y,x: " + this.LR_LAT + "," + this.LR_LON);
            //System.out.println("we reached intersect and it's: " + (UL_LON <= x && UL_LAT <= y && LR_LAT >= y && LR_LON >= x));
            //return (UL_LON <= x && UL_LAT <= y && LR_LAT >= y && LR_LON >= x);
            //boolean checkX = (this.UL_LON <= x && this.LR_LON >= x) || (this.LR_LON <= x && this.UL_LON >= x);
            //boolean checkY = (this.UL_LAT <= y && this.LR_LAT >= y) || (this.LR_LAT <= y && this.UL_LAT >= y);
            //System.out.println("we reached intersect and it's x: " +  checkX);
            //System.out.println("we reached intersect and it's y: " + checkY);

           /* if (checkX && checkY) {
                System.out.println("we reached intersect and it's true");
                return true;
            }
            System.out.println("we reached intersect and it's false");
            return false;*/
        }

        private boolean check_LON(double user_LON, double tile_UL_LON, double tile_LR_LON){
            return (user_LON >= tile_UL_LON && user_LON <= tile_LR_LON);
        }

        private boolean check_LAT(double user_LAT, double tile_UL_LAT, double tile_LR_LAT){
            return (user_LAT <= tile_UL_LAT && user_LAT >= tile_LR_LAT);
        }

        @Override
        public int compareTo(QuadTreeNode node) {
            if (this.UL_LAT < node.UL_LAT) {
                return 1;
            } else if (this.UL_LAT > node.UL_LAT) {
                return -1;
            } else {
                if (this.UL_LON < node.UL_LON) {
                    return -1;
                } else if (this.UL_LON > node.UL_LON) {
                    return 1;
                }
                return 0;
            }
        }

    }
}
