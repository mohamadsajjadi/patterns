<div dir="rtl">

<h2>آزمایش شماره V (بازآرایی) - اعمال الگوی Facade</h2>

<h3>هدف</h3>
<p>ساده‌سازی فرآیند راه‌اندازی و تعامل با کامپونت‌های پیچیده در سیستم‌های تجزیه‌گر (Parser) با استفاده از الگوی Facade.</p>

<h3>مشکل</h3>
<ul>
  <li>تعامل مستقیم با چندین کامپونت مانند <code>CodeGenerator</code>، <code>ParseTable</code> و <code>Rule</code> نیازمند راه‌اندازی و مدیریت جداگانه است.</li>
  <li>این رویکرد باعث پیچیدگی کد مشتری، وابستگی زیاد به جزئیات داخلی، و کاهش خوانایی و قابلیت نگهداری می‌شود.</li>
</ul>

<h3>راه‌حل</h3>
<p>ایجاد کلاس <code>ParserFacade</code> برای:</p>
<ul>
  <li>پنهان کردن پیچیدگی‌های راه‌اندازی کامپونت‌ها.</li>
  <li>ارائه یک رابط ساده و یکپارچه به مشتری.</li>
</ul>

<h3>نمونه کد</h3>

```java
public class ParserFacade {
    private Parser parser;

    public ParserFacade() {
        parser = new Parser();
    }

    public void parseFile(String filePath) {
        try {
            Scanner sc = new Scanner(new File(filePath));
            parser.startParse(sc);
        } catch (FileNotFoundException e) {
            ErrorHandler.printError(e.getMessage());
        }
    }
}
```

<h3>مزایای کلیدی الگوی Facade</h3>
<ul>
  <li><strong>ساده‌سازی کد مشتری:</strong> مشتری تنها با <code>ParserFacade</code> تعامل دارد و نیازی به مدیریت جزئیات داخلی کامپونت‌ها ندارد.</li>
  <li><strong>کاهش وابستگی‌ها:</strong> تغییرات در کامپونت‌های داخلی (مانند <code>CodeGenerator</code> یا <code>ParseTable</code>) تأثیر کمتری بر کد مشتری می‌گذارد.</li>
  <li><strong>افزایش خوانایی:</strong> رابط تمیز و سطح بالا، کد را برای توسعه‌دهندگان قابل‌درک‌تر و قابل‌نگهداری می‌کند.</li>
  <li><strong>نگهداری آسان‌تر:</strong> تغییرات در منطق راه‌اندازی تنها در کلاس <code>ParserFacade</code> اعمال می‌شوند و نیازی به اصلاح کد مشتری نیست.</li>
</ul>

با استفاده از الگوی Facade، ما موفق شدیم کد مشتری را برای راه‌اندازی و تعامل با `Parser` ساده کنیم. حالا مشتری تنها باید با یک کلاس ساده (`ParserFacade`) تعامل کند و پیچیدگی‌های راه‌اندازی کامپوننت‌ها از طریق این رابط یکپارچه پنهان شده است. این باعث می‌شود کد آسان‌تر نگهداری، گسترش و استفاده شود.

<h3>نتیجه‌گیری نهایی</h3>
<ul>
  <li>✅ پیچیدگی‌های سیستم به‌طور مؤثر مدیریت شد.</li>
  <li>✅ کد مشتری ساده‌تر و با قابلیت نگهداری بالاتر طراحی شد.</li>
  <li>✅ قابلیت توسعه و انعطاف‌پذیری سیستم بهبود یافت.</li>
</ul>

**facade دوم**  

ما از **Facade** استفاده می‌کنیم تا کار با **SymbolTable** را ساده‌تر کنیم. این الگو پیچیدگی‌های جدول نمادها را پشت یک رابط تمیز پنهان می‌کند، اما همه قابلیت‌های اصلی آن حفظ می‌شود.  

**کارهای اصلی SymbolTableFacade:**  
- اضافه کردن کلاس، متد، فیلد و متغیر  
- جست‌وجو بین نمادها و پارامترها  
- ارتباط پشت‌صحنه با **SymbolTable** بدون کم‌کردن کارایی  

نتیجه؟ **کد تمیزتر، فهمیدنش راحت‌تر، دردسر کمتر.**


### **کد کامل SymbolTableFacade.java**
```java
public class SymbolTableFacade {
    private SymbolTable symbolTable;

    public SymbolTableFacade(Memory memory) {
        this.symbolTable = new SymbolTable(memory);
    }

    // Adds a class to the symbol table
    public void defineClass(String className) {
        symbolTable.addClass(className);
    }

    // Sets the superclass for a given class
    public void setSuperClass(String className, String superClass) {
        symbolTable.setSuperClass(superClass, className);
    }

    // Adds a field to a class
    public void defineField(String className, String fieldName, SymbolType fieldType) {
        symbolTable.setLastType(fieldType);
        symbolTable.addField(fieldName, className);
    }

    // Adds a method to a class
    public void defineMethod(String className, String methodName, SymbolType returnType, int codeAddress) {
        symbolTable.setLastType(returnType);
        symbolTable.addMethod(className, methodName, codeAddress);
    }

    // Adds a parameter to a method
    public void defineMethodParameter(String className, String methodName, String parameterName,
            SymbolType parameterType) {
        symbolTable.setLastType(parameterType);
        symbolTable.addMethodParameter(className, methodName, parameterName);
    }

    // Adds a local variable to a method
    public void defineMethodLocalVariable(String className, String methodName, String variableName,
            SymbolType variableType) {
        symbolTable.setLastType(variableType);
        symbolTable.addMethodLocalVariable(className, methodName, variableName);
    }

    // Retrieves the address of a keyword from the symbol table
    public Address getAddress(String keywordName) {
        return symbolTable.get(keywordName);
    }

    // Retrieves a field symbol from a class
    public Symbol getField(String className, String fieldName) {
        return symbolTable.get(fieldName, className);
    }

    // Retrieves a variable symbol from a method in a class
    public Symbol getVariable(String className, String methodName, String variableName) {
        return symbolTable.get(className, methodName, variableName);
    }

    // Retrieves the next parameter of a method
    public Symbol getNextParameter(String className, String methodName) {
        return symbolTable.getNextParam(className, methodName);
    }

    // Consumes the next parameter of a method
    public void consumeNextParameter(String className, String methodName) {
        symbolTable.startCall(className, methodName);
    }

    // Retrieves the return type of a method
    public SymbolType getMethodReturnType(String className, String methodName) {
        return symbolTable.getMethodReturnType(className, methodName);
    }

    // Retrieves the return address of a method
    public int getMethodReturnAddress(String className, String methodName) {
        return symbolTable.getMethodReturnAddress(className, methodName);
    }

    // Retrieves the address of a method in a class
    public int getMethodAddress(String className, String methodName) {
        return symbolTable.getMethodAddress(className, methodName);
    }

    // Retrieves the caller address of a method
    public int getMethodCallerAddress(String className, String methodName) {
        return symbolTable.getMethodCallerAddress(className, methodName);
    }

    public void setLastType(SymbolType type) {
        symbolTable.setLastType(type);
    }

}
```

**خلاصه فواید و نتیجه‌گیری:**  

✅ **مزایای SymbolTableFacade:**  
1. **کپسوله‌سازی:** پیچیدگی‌های `SymbolTable` پشت فاساد مخفی می‌شود.  
2. **خوانایی:** کد کاربر (مثل `CodeGenerator`) تمیزتر و قابل‌فهم‌تر می‌شود.  
3. **انعطاف‌پذیری:** تغییرات آینده در `SymbolTable` به کد کاربر آسیب نمی‌زند.  
4. **کاربرپسند:** API ساده‌تر، خطای کمتر، بازدهی بالاتر.  

**نتیجه نهایی:**  
- **کارکرد سیستم مثل قبل، اما با رابط ساده‌تر.**  
- **کد تمیزتر و نگهداری آسان‌تر.**  

</div>
