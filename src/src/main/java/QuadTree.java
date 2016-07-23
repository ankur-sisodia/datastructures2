/**
 * Created by Jason on 7/21/16.
 */

/**
 * Created by asisodia on 7/21/2016.
 */

import java.util.ArrayList;

public class QuadTree {
    //image map and four children
    private QuadTreeNode root;
    protected int depth;

    public QuadTree() {
        root = new QuadTreeNode(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT, MapServer.ROOT_LRLAT);
        root.upper_left = new QuadTreeNode("1", root.UL_LAT, root.UL_LON,
                (root.UL_LAT + root.LR_LAT) / 2, (root.UL_LON + root.LR_LON) / 2);
        root.upper_right = new QuadTreeNode("2", root.UL_LAT, (root.UL_LON + root.LR_LON)/2,
                root.LR_LAT, (root.UL_LON + root.LR_LON) / 2);
        root.lower_left = new QuadTreeNode("3", (root.UL_LAT + root.LR_LAT)/2, root.UL_LON,
                root.LR_LAT, (root.UL_LON + root.LR_LON) / 2);
        root.lower_right = new QuadTreeNode("4", (root.UL_LAT + root.LR_LAT) / 2, (root.UL_LON + root.LR_LON) / 2,
                root.LR_LAT, root.LR_LON);
    }

    public int calcDepthPix(double ullon, double lrlon, double w) {
        depth = 0;
        double widthTile = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON);
        while (!((widthTile/MapServer.TILE_SIZE) < (Math.abs(lrlon - ullon)/(w)))) {
            widthTile = widthTile/(Math.pow(2.0, depth));
            depth++;
        }
        return (depth - 1);
    }

    public ArrayList<QuadTreeNode> traverseHelper(ArrayList<QuadTreeNode> arr, QuadTreeNode node, double ullat, double ullon) {

        //System.out.println("level: " + node.getLevel() + " depth: " + depth);
        if (node.getLevel() == depth) {
            System.out.println("node:" + node.imageName);
            arr.add(node);
        } else {
            //if (node.upper_left.intersect(ullat, ullon)) {

            traverseHelper(arr, node.upper_left, node.upper_left.UL_LAT, node.upper_left.UL_LON);

            //}
            //if (node.upper_right.intersect(ullat, ullon)) {

            traverseHelper(arr, node.upper_right, node.upper_right.UL_LAT, node.upper_right.UL_LON);

            //}
            //if (node.lower_left.intersect(ullat, ullon)) {

            traverseHelper(arr, node.lower_left, node.lower_left.UL_LAT, node.lower_left.UL_LON);

            //}
            //if (node.lower_right.intersect(ullat, ullon)) {

            traverseHelper(arr, node.lower_right, node.lower_right.UL_LAT, node.lower_right.UL_LON);

            //}
        }
        //System.out.println("size: " + arr.size());
        return arr;
    }

    public ArrayList<QuadTreeNode> traverseTree(double ullat, double ullon, double lrlat, double lrlon, double w, double h) {
        ArrayList<QuadTreeNode> arr = new ArrayList();
        depth = calcDepthPix(ullon, lrlon, w);
        System.out.println("depth: " + depth);
        return traverseHelper(arr, root, ullat, ullon);
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

            if (this.getLevel() < 8) {
                upper_left = new QuadTreeNode(imageName + "1", UL_LAT, UL_LON, (UL_LAT + LR_LAT)/2, (UL_LON + LR_LON)/2);
                upper_right = new QuadTreeNode(imageName + "2", UL_LAT, (UL_LON + LR_LON)/2, (UL_LAT + LR_LAT)/2, LR_LON);
                lower_left = new QuadTreeNode(imageName + "3", (UL_LAT + LR_LAT)/2, UL_LON, LR_LAT, (UL_LON + LR_LON)/2);
                lower_right = new QuadTreeNode(imageName + "4", (UL_LAT + LR_LAT)/2, (UL_LON + LR_LON)/2, LR_LAT, LR_LON);
            }
        }

        public int getLevel() {
            if (imageName.equals("root.png")) {
                return 0;
            }
            return imageName.length();
        }

        public boolean intersect(double y, double x) {
            System.out.println("y,x: " + y + "," + x);
            System.out.println("UL y,x: " + UL_LAT + "," + UL_LON);
            System.out.println("LR y,x: " + LR_LAT + "," + LR_LON);
            //System.out.println("we reached intersect and it's: " + (UL_LON <= x && UL_LAT <= y && LR_LAT >= y && LR_LON >= x));
            //return (UL_LON <= x && UL_LAT <= y && LR_LAT >= y && LR_LON >= x);
            boolean checkX = (UL_LON <= x && LR_LON >= x) || (LR_LON <= x && UL_LON >= x);
            boolean checkY = (UL_LAT <= y && LR_LAT >= y) || (LR_LAT <= y && UL_LAT >= y);
            System.out.println("we reached intersect and it's x: " +  checkX);
            System.out.println("we reached intersect and it's y: " + checkY);
            if (checkX && checkY) {
                System.out.println("we reached intersect and it's true");
                return true;
            }
            System.out.println("we reached intersect and it's false");
            return false;
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