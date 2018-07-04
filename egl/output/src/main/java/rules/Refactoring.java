package rules;

// Noncompliant@+1
//TODO R1: Revisar.
class Refactoring {
    Refactoring(Refactoring mc) {
    }

    int foo1() {
        return 0;
    }

    void foo2(int value) {
    }

    int foo3(int value) {
        return 0;
    }

    Object foo4(int value) {
        return null;
    }

    Refactoring foo5(Refactoring value) {
        return null;
    }

    // Noncompliant@+1
    //TODO R1: revisar
    int foo6(int value, String name) {
        return 0;
    }

    int foo7(int... values) {
        return 0;
    }
}
