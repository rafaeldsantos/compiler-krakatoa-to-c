//Rafael Danilo dos Santos RA 408654
package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type voidType = new TypeVoid();
    public static Type undefinedType = new TypeUndefined();
    //public static Type identType = new TypeIdent();

    public String getName() {
        return name;
    }

    abstract public String getCname();

    protected String name;
}
