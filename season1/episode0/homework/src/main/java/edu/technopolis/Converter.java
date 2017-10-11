package edu.technopolis;

class Converter {

    static String convertTo10(long[] binaryNum){
        long[] number = new long[binaryNum.length];
        int j = 0;
        for(int i = 0; i < binaryNum.length*64; i++){
            if(i != 0 && (i-63)%64 == 0)
                continue;
            setBit(number, getBit(binaryNum, i), j);
            j++;
        }

        long[] temp = new long[(int)(Math.ceil(number.length*1.25f))]; //число в двоично десятичной системе


        //прибавляем 3 по всем тетрадам
        AddToAllTetrads(temp, 3);

        for(int k = number.length - 1; k >= 0; k--) {
            long mask = 1L << 63;

            for (int i = 0; i < 64; i++) {
                long num = number[k] & mask;
                number[k] <<= 1L;

                for(int n = 0; n < temp.length; n++) {
                    long firstBit = temp[n] & (1L << 63);
                    temp[n] <<= 1;
                    if (num != 0) {
                        temp[n] = temp[n] | (1L); //последний бит 1
                    }
                    Correction(temp, n, firstBit);
                    num = firstBit;
                }
            }
        }

        //отнимаем 3 по всем тетрадам
        AddToAllTetrads(temp, -3);

        //преобразование из 2/10 в 10
        return ConvertToString(temp);
    }

    private static long getBit(long[] number, int i){
        int arrayNumber = i/64;
        if(arrayNumber > number.length - 1)
            return 0;
        return ((number[arrayNumber] >> (i%64)) & 1L);
    }
    private static void setBit(long[] number, long value, int i){
        int arrayNumber = i/64;
        try {
            if(value == 0)
                number[arrayNumber] = number[arrayNumber] & ~(1L << (i%64));
            else
                number[arrayNumber] = number[arrayNumber] | (1L << (i%64));
        } catch (ArrayIndexOutOfBoundsException e){
            return;
        }

    }
    private static void AddToAllTetrads(long[] temp, long num){
        for(int k = 0; k < temp.length; k++) {
            for (int i = 0; i < 16; i++) {
                temp[k] += num << i * 4;
            }
        }
    }
    private static void Correction(long[] temp, int n, long firstBit){
        for (int j = 0; j < 15; j++) {
            long tetraMask;
            tetraMask = 1L << (j + 1) * 4;

            if ((temp[n] & tetraMask) != 0) {
                temp[n] += 3L << j * 4;
            } else {
                temp[n] -= 3L << j * 4;
            }
        }

        if (firstBit != 0)
            temp[n] += 3L << (15 * 4);
        else
            temp[n] -= 3L << (15 * 4);
    }
    private static String ConvertToString(long[] temp){
        String strNum = "";

        L: for(int k = 0; k < temp.length; k++) {
            for (int i = 0; i < 16; i++) {
                long digit = 0;
                for (int j = 0; j < 4; j++) {
                    digit += (temp[k] & 1L) << j;
                    temp[k] >>= 1;
                }
                strNum += digit;
                if(isZero(temp))
                    break L;
            }
        }
        strNum = new StringBuilder(strNum).reverse().toString();
        return strNum;
        //return strNum.replace("0", "");
    }
    private static boolean isZero(long[] temp){
        for(long i: temp) {
            if (i < 0)
                i = ~i;
            if (i != 0)
                return false;
        }
        return true;
    }
}

