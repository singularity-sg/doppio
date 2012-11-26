
package classes.test;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/* Still needs to be tested:
compareAndSwapObject(Ljava/lang/Object;JLjava/lang/Object;Ljava/lang/Object;)Z
putOrderedObject(Ljava/lang/Object;JLjava/lang/Object;)V
defineClass(Ljava/lang/String;[BIILjava/lang/ClassLoader;Ljava/security/ProtectionDomain;)Ljava/lang/Class;
*/

public class UnsafeOps {

  public static Unsafe getUnsafe() {
    try {
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      return (Unsafe)f.get(null);
    } catch (Exception e) {
      return null;
    }
  }

  public static void main(String[] args) {
    Unsafe unsafe = getUnsafe();
    // These will vary from platform to platform,
    // so just make sure they exist
    int addrSize = unsafe.addressSize();
    int abo = unsafe.arrayBaseOffset(int[].class);
    int ais = unsafe.arrayIndexScale(int[].class);

    Object f;
    try {
      f = unsafe.allocateInstance(Foo.class);
      System.out.println(((Foo)f).a);
      System.out.println(((Foo)f).b);
      System.out.println(((Foo)f).c);
    } catch (InstantiationException e) {
      System.out.println(e);
      return;
    }

    Field fc;
    try {
      fc = Foo.class.getDeclaredField("c");
    } catch (NoSuchFieldException e) {
      System.out.println(e);
      return;
    }
    long offset = unsafe.objectFieldOffset(fc);
    System.out.println(unsafe.getObject(f,offset));
    unsafe.putObject(f,offset, "hello Unsafe");
    System.out.println(unsafe.getObject(f,offset));
    System.out.println(((Foo)f).c);
  }

}

class Foo {
  int a = 7;
  double b = 5.9;
  String c;
}