package net.maesierra.adventOfCode2025.utils;

public class MathHelper {


    public static boolean nonZero(double num) {
        return num < -0.0001 || num > 0.0001;
    }


    public static class ReducedRowEchelon {


        /**
         * Puts a matrix into reduced row echelon form
         *
         * @param m input matrix
         *
         * @return 2D result matrix
         */
        public static double[][] reducedRowEchelon(double[][] m){

            int columnIndex = 0;
            int cursor;

            // number of rows and columns in matrix
            int nRows = m.length;
            int nColumns = m[0].length;
            double [][] matrix = new double[nRows][nColumns];
            for (int i = 0; i < nRows; i++) {
                System.arraycopy(m[i], 0, matrix[i], 0, nColumns);
            }

            loop:
            for(int rowIndex = 0; rowIndex < nRows; rowIndex++) {
                if(nColumns <= columnIndex){
                    break;
                }
                cursor = rowIndex;
                while(!nonZero(matrix[cursor][columnIndex])){
                    cursor++;
                    if(nRows == cursor){
                        cursor = rowIndex;
                        columnIndex++;
                        if(nColumns == columnIndex){
                            break loop;
                        }
                    }

                }

                rowSwap(matrix, cursor, rowIndex);
                if(nonZero(matrix[rowIndex][columnIndex])){
                    rowScale(matrix, rowIndex, (1/matrix[rowIndex][columnIndex]));
                }

                for(cursor = 0; cursor < nRows; cursor++){
                    if(cursor != rowIndex){
                        rowAddScale(matrix, rowIndex, cursor,((-1) * matrix[cursor][columnIndex]));
                    }
                }
                columnIndex++;
            }
            return matrix;
        }

        /**
         * Swap positions of 2 rows
         *
         * @param matrix matrix before row additon
         * @param rowIndex1 int index of row to swap
         * @param rowIndex2 int index of row to swap
         *
         */
        private static void rowSwap(double[][] matrix, int rowIndex1,
                                              int rowIndex2){
            // number of columns in matrix
            int numColumns = matrix[0].length;

            // holds number to be swapped
            double hold;

            for(int k = 0; k < numColumns; k++){
                hold = matrix[rowIndex2][k];
                matrix[rowIndex2][k] = matrix[rowIndex1][k];
                matrix[rowIndex1][k] = hold;
            }
        }

        /**
         * Multiplies a row by a scalar
         *
         * @param matrix matrix before row additon
         * @param rowIndex int index of row to be scaled
         * @param scalar double to scale row by
         *
         */
        private static void rowScale(double[][] matrix, int rowIndex,
                                               double scalar){
            // number of columns in matrix
            int numColumns = matrix[0].length;

            for(int k = 0; k < numColumns; k++){
                matrix[rowIndex][k] *= scalar;
            }
        }

        /**
         * Adds a row by the scalar of another row
         * row2 = row2 + (row1 * scalar)
         * @param matrix matrix before row additon
         * @param rowIndex1 int index of row to be added
         * @param rowIndex2 int index or row that row1 is added to
         * @param scalar double to scale row by
         *
         */
        private static void rowAddScale(double[][] matrix, int rowIndex1,
                                                  int rowIndex2, double scalar){
            // number of columns in matrix
            int numColumns = matrix[0].length;

            for(int k = 0; k < numColumns; k++){
                matrix[rowIndex2][k] += (matrix[rowIndex1][k] * scalar);
            }
        }
    }

}
