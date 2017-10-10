package main.java.edu.technopolis;

public class BigNumber {
    private long[] number;

    BigNumber(){
        number = new long[4];
    }
    private BigNumber(int initialSize){
        number = new long[initialSize];
    }

    void setValue(long value){
        number[0] = value;
        for(int i = 1; i < number.length; i++)
            number[i] = 0;
    }

    BigNumber add(BigNumber term){
        if(number.length < term.number.length){
            return term.add(this);
        }
        int size = number.length;
        if(number[size-1] > 0)
            size++;

        BigNumber result = new BigNumber(size);
        byte remain = 0;
        for(int i = 0; i<number.length; i++){
            long second;
            if(i >= term.number.length)
                second = 0;
            else
                second = term.number[i];
            result.number[i] = number[i] + second + remain;

            if(result.number[i] < 0){
                remain = 1;
                result.number[i] -= Long.MIN_VALUE;
            } else
                remain = 0;
        }
        return result;
    }

    @Override
    public String toString() {
        return Converter.convertTo10(number);
    }
}
