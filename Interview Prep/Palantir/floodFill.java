//Nick White Implementation https://www.youtube.com/watch?v=aehEcTEPtCs
//Taylan Unal
class Solution {
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        if(image[sr][sc] == newColor)
            return image; //means the image is complete. Given starting pixel should be different.
        fill(image, sr, sc, image[sr][sc], newColor);
        return image;
    }
    
    public void fill(int[][] image, int sr, int sc, int color, int newColor){ //same signature as above, but also takes oldColor
        //only changes current (one) pixel
        if(sr<0 || sc<0 || sr >= image.length || sc >= image[0].length || image[sr][sc] != color)
            return; //ends here
        
        image[sr][sc]=newColor; //at current row/col, make new color. 
        fill(image, sr-1, sc, color, newColor); //row before
        fill(image, sr+1, sc, color, newColor); //row after
        fill(image, sr, sc-1, color, newColor); //col before
        fill(image, sr, sc+1, color, newColor); //col after
    }
}

// image = [[1,1,1],
//         [1,1,0],
//         [1,0,1]];
// sr = 1, sc = 1, newColor = 2
    
// Output: [[2,2,2],
//          [2,2,0],
//          [2,0,1]];