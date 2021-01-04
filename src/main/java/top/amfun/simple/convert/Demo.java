package top.amfun.simple.convert;

/**
 * @author zhaoxg
 * @date 2020/11/17 14:39
 */
public class Demo {
    public static void main(String[] args) {
        PersonInfo info = PersonInfo.builder().name("zs").build();
        PersonInfo info2 = new PersonInfo.Builder().name("zz").age(44).build();
        PersonInfo info3 = PersonInfo.builder().name("zs").build();
        System.out.printf("info equals info3 ? %s", info==info3);
    }
}
class PersonInfo {

    public static Builder builder() {
        return new Builder();
    }

    private PersonInfo(Builder builder){
        name = builder.name;
        age = builder.age;
        address = builder.address;
    }

    private String name;
    private int age;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder {
        private String name;
        private int age;
        private String address;

        public PersonInfo.Builder name(String name) {
            this.name = name;
            return this;
        }
        public PersonInfo.Builder age(int age) {
            this.age = age;
            return this;
        }
        public PersonInfo.Builder address(String address) {
            this.address = address;
            return this;
        }

        public PersonInfo build() {
            return new PersonInfo(this);
        }
    }
}


