package edu.technopolis;

/**
 * Это просто точка входа для первого домашнего задания.
 * Содержит только метод main и ничего кроме. Никакой дполнительной нагрузки не несёт.
 * Является интерфейсом для
 * <ul>
 *     <li>Демонстрации возможности метода main в интрефейсе</li>
 *     <li>Экономии слова public</li>
 * </ul>
 */
public interface EntryPoint {
    static void main(String... args) {
        FibonacciAlgorithm algorithm = new DummyFibonacciAlgorithm();
        System.out.println(algorithm.evaluate(Integer.parseInt(args[0])));

        long time = System.currentTimeMillis();
        FibonacciAlgorithm myAlgorithm = new MyFibonacciAlgorithm();
        System.out.println(myAlgorithm.evaluate(Integer.parseInt(args[0])));
        System.out.println(System.currentTimeMillis() - time);
    }
}
