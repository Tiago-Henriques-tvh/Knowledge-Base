# Parameter Passing and Move Semantics

## Pass by Value

Copies the object, invoking the copy constructor.

```cpp
void func(Foo f) { /*...*/ }  // `f` is a copy of the argument
Foo x;
func(x);  // `x` is passed by value, invoking Foo's copy constructor
```

## Pass by Reference

Passes a reference to the object, allowing modification without copying.

```cpp
void func(Foo& f) { /*...*/ }  // `f` is a reference to the original object
Foo x;
func(x);  // `x` is passed by reference, no copy is made
```

## Pass by Const Reference

Passes a reference but ensures the object cannot be modified.

```cpp
void func(const Foo& f) { /*...*/ }  // `f`is a const reference
Foo x;
func(x);  //`x` is passed by reference, no copy, and cannot be modified inside func
```

## Pass by Pointer

Passes a pointer to the object, allowing efficient access and modification.

```cpp
void func(Foo* f) { /*...*/ }  // `f` is a pointer to the object
Foo x;
func(&x);  // Passes the address of `x`, allows modifying the original
```

## Move Semantics

Optimizes performance by allowing the transfer of resources (not copying them), especially for temporary objects.

```cpp
Foo a;
Foo b = std::move(a);  // Move constructor is called, resources from `a` are transferred to `b`
```

### Comparison: Implicit vs. Explicit Move Semantics

| Aspect                   | **Implicit Move Semantics**                                | **Explicit Move Semantics**                         |
| ------------------------ | ---------------------------------------------------------- | --------------------------------------------------- |
| **Invocation**           | Automatic when the object is an rvalue.                    | Manually triggered using `std::move()`.             |
| **Type of Object**       | Happens with rvalues (temporary objects).                  | Used to cast lvalues (named objects) to rvalues.    |
| **Typical Use Case**     | Returning objects from functions or temporary expressions. | Moving named objects (lvalues) explicitly.          |
| **Example**              | Returning by value, passing rvalue to a function.          | Moving an object with std::move().                  |
| **Compiler Involvement** | Handled by the compiler automatically.                     | Requires programmer intervention using std::move(). |

## Summary

```cpp
class Foo {
public:
    Foo() { /* default constructor */ }
    Foo(const Foo& other) { /* copy constructor */ }
    Foo(Foo&& other) noexcept { /* move constructor */ }
    Foo& operator=(Foo&& other) noexcept { /* move assignment operator */ }
};

Foo createFoo() {
    Foo temp;
    return temp;  // Implicit move (returns a temporary)
}

int main() {
    Foo f1;
    Foo f2 = createFoo();  // Implicit move (assigns the temporary returned by createFoo)

    Foo f3 = std::move(f2);  // Explicit move (moves f2 into f3)
}
```

# Smart pointers

| **Type of Smart Pointer** | **Description**                                                                                                                          | **Ownership Model**                                                       | **Thread Safety**                                                 | **Use Cases**                                                                                                          |
| ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------- | ----------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| `std::unique_ptr`         | Owns a resource exclusively. No other smart pointer can own the same resource. Automatically deletes resource when it goes out of scope. | **Exclusive ownership**: Only one pointer can own the resource at a time. | Not thread-safe (unless explicitly synchronized).                 | Use when you want a single owner for a resource, like RAII (Resource Acquisition Is Initialization).                   |
| `std::shared_ptr`         | Shares ownership of a resource. The resource is deleted when the last `shared_ptr` referencing it is destroyed.                          | **Shared ownership**: Multiple pointers can own the resource.             | Thread-safe for reference counting but not for the object itself. | Use when you need multiple owners for the same resource, like in shared data scenarios.                                |
| `std::weak_ptr`           | A non-owning smart pointer that tracks a resource managed by a `shared_ptr`. Does not affect reference count.                            | **Non-owning observer**: Only observes but does not own the resource.     | Thread-safe for reference counting.                               | Use to break cyclic references when working with `shared_ptr`, or to observe an object without extending its lifetime. |

**Notes:**
Reference Counting: shared_ptr uses a reference count to track how many pointers own the resource. When the count reaches zero, the resource is automatically deleted.
Ownership Transfer: With unique_ptr, you can transfer ownership using std::move. However, shared_ptr allows shared ownership without the need for transfer.
Cyclic References: weak_ptr helps in breaking cyclic references that could cause memory leaks when using shared_ptr.

# Templates

```c++
#include <iostream>
using namespace std;

// Function template to add two values of any type
template <typename T>
T add(T a, T b) {
    return a + b;
}

// Class template to create a Stack of any type
template <typename T>
class Stack {
private:
    vector<T> elements;

public:
    void push(T value) {
        elements.push_back(value);
    }

    void pop() {
        if (!elements.empty()) {
            elements.pop_back();
        } else {
            cout << "Stack is empty!" << endl;
        }
    }

    T top() {
        if (!elements.empty()) {
            return elements.back();
        }
        throw runtime_error("Stack is empty!");
    }

    bool isEmpty() {
        return elements.empty();
    }
};

int main() {
    // Using the template with different types
    cout << "Add integers: " << add(3, 4) << endl;        // Integers
    cout << "Add doubles: " << add(2.5, 3.7) << endl;     // Doubles
    cout << "Add strings: " << add(string("Hello, "), string("World!")) << endl; // Strings

    // Stack of integers
    Stack<int> intStack;
    intStack.push(10);
    intStack.push(20);
    cout << "Top of intStack: " << intStack.top() << endl;

    // Stack of strings
    Stack<string> stringStack;
    stringStack.push("Hello");
    stringStack.push("World");
    cout << "Top of stringStack: " << stringStack.top() << endl;

    return 0;
}
```

## Key Concepts of Templates

- Generic Programming: Templates enable writing generic code that works for any data type.
- Type Deduction: The compiler automatically deduces the correct type based on the arguments passed to the templated function or class.
- Code Reusability: Templates reduce redundancy by allowing a single implementation to work with multiple data types.

## Types of Templates

- Function Templates: As shown above, they allow functions to operate on generic types.
- Class Templates: These allow defining classes with generic types, useful for data structures like stacks, queues, and lists.
- Template Specialization: You can create specialized implementations of templates for specific types when the generic version is not sufficient.
- Templates are essential in C++ for creating libraries like the Standard Template Library (STL), which provides generic data structures and algorithms such as vector, list, map, sort(), and more.
