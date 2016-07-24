
/**
 * Created by asisodia on 7/21/2016.
 */
import java.util.ArrayList;

public class QuadTree {
    //image map and four children
    private QuadTreeNode root;
    private int depth;

    public QuadTree() {
        root = new QuadTreeNode(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON);
        root.upperLeft = new QuadTreeNode("1", root.ulLAT, root.ulLON,
                (root.ulLAT + root.lrLAT) / 2, (root.ulLON + root.lrLON) / 2);
        root.upperRight = new QuadTreeNode("2", root.ulLAT, (root.ulLON + root.lrLON) / 2,
                (root.ulLAT + root.lrLAT) / 2, root.lrLON);
        root.lowerLeft = new QuadTreeNode("3", (root.ulLAT + root.lrLAT) / 2, root.ulLON,
                root.lrLAT, (root.ulLON + root.lrLON) / 2);
        root.lowerRight = new QuadTreeNode("4", (root.ulLAT + root.lrLAT) / 2,
                (root.ulLON + root.lrLON) / 2, root.lrLAT, root.lrLON);
    }

    public int getDepth() {
        return depth;
    }

    public int calcMaxDepth(double ullon, double lrlon, double w) {
        depth = 0;
        double queryBoxDPP = Math.abs(lrlon - ullon) / w;
        double tileDPP = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                / MapServer.TILE_SIZE;
        while (depth <= 6 && tileDPP > queryBoxDPP) {
            depth++;
            tileDPP /= 2;
        }
        return depth;
    }

    public ArrayList<QuadTreeNode> traverseHelper(ArrayList<QuadTreeNode> arr, QuadTreeNode node,
                                                  double ullat, double ullon, double lrlat,
                                                  double lrlon) {
        //System.out.println("level: " + node.getLevel() + " depth: " + depth);
        if (node.getLevel() >= depth) {
            //System.out.println("node:" + node.imageName);
            arr.add(node);
        } else {
            if (node.upperLeft.intersect(ullat, ullon, lrlat, lrlon)) {
                //traverseHelper(arr, node.upperLeft, node.upperLeft.ulLAT, node.upperLeft.ulLON);
                traverseHelper(arr, node.upperLeft, ullat, ullon, lrlat, lrlon);
            }
            if (node.upperRight.intersect(ullat, ullon, lrlat, lrlon)) {
                traverseHelper(arr, node.upperRight, ullat, ullon, lrlat, lrlon);
            }
            if (node.lowerLeft.intersect(ullat, ullon, lrlat, lrlon)) {
                traverseHelper(arr, node.lowerLeft, ullat, ullon, lrlat, lrlon);
            }
            if (node.lowerRight.intersect(ullat, ullon, lrlat, lrlon)) {

                traverseHelper(arr, node.lowerRight, ullat, ullon, lrlat, lrlon);

            }
        }
        //System.out.println("size: " + arr.size());
        return arr;
    }

    public ArrayList<QuadTreeNode> traverseTree(double ullat, double ullon, double lrlat,
                                                double lrlon, double w, double h) {
        ArrayList<QuadTreeNode> arr = new ArrayList();
        depth = calcMaxDepth(ullon, lrlon, w);
        //System.out.println("depth: " + depth);
        return traverseHelper(arr, root, ullat, ullon, lrlat, lrlon);
    }

    public class QuadTreeNode implements Comparable<QuadTreeNode> {
        protected QuadTreeNode upperLeft, upperRight;
        protected QuadTreeNode lowerLeft, lowerRight;
        protected double ulLAT, ulLON, lrLAT, lrLON;
        protected String imageName;

        public QuadTreeNode (double ulLAT, double ulLON, double lrLAT, double lrLON) {
            imageName = "root";
            this.ulLAT = ulLAT; // a
            this.ulLON = ulLON; // b
            this.lrLAT = lrLAT; // c
            this.lrLON = lrLON; // d
        }

        public QuadTreeNode (String imageName, double ulLAT, double ulLON, double lrLAT, double lrLON) {
            this.imageName = imageName;
            this.ulLAT = ulLAT; // a
            this.ulLON = ulLON; // b
            this.lrLAT = lrLAT; // c
            this.lrLON = lrLON; // d

            if (this.getLevel() < 7) {
                upperLeft = new QuadTreeNode(imageName + "1", ulLAT, ulLON,
                        (ulLAT + lrLAT) / 2, (ulLON + lrLON) / 2);
                upperRight = new QuadTreeNode(imageName + "2", ulLAT, (ulLON + lrLON) / 2,
                        (ulLAT + lrLAT) / 2, lrLON);
                lowerLeft = new QuadTreeNode(imageName + "3", (ulLAT + lrLAT) / 2, ulLON,
                        lrLAT, (ulLON + lrLON) / 2);
                lowerRight = new QuadTreeNode(imageName + "4", (ulLAT + lrLAT) / 2,
                        (ulLON + lrLON) / 2, lrLAT, lrLON);

            }
        }

        public int getLevel() {
            if (imageName.equals("root")) {
                return 0;
            }
            return imageName.length();
        }

        public boolean intersect(double userULLAT, double userULLON, double userLRLAT,
                                 double userLRLON) {
            if(this.lrLON < userULLON) {
                return false;
            }

            if(this.ulLON > userLRLON) {
                return false;
            }

            if(this.lrLAT > userULLAT) {
                return false;
            }

            if(this.ulLAT < userLRLAT) {
                return false;
            }

            return true;
        }

        @Override
        public int compareTo(QuadTreeNode node) {
            if (this.ulLAT < node.ulLAT) {
                return 1;
            } else if (this.ulLAT > node.ulLAT) {
                return -1;
            } else {
                if (this.ulLON < node.ulLON) {
                    return -1;
                } else if (this.ulLON > node.ulLON) {
                    return 1;
                }
                return 0;
            }
        }

    }
}
