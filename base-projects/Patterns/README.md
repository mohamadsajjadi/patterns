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

****
الگوی استراتژی (State/Strategy Pattern) یا پلی‌مورفیسم به جای شرط‌ها برای حذف دستورات شرطی پیچیده مانند if-else یا switch استفاده می‌شود. هدف این الگو، نگهداری بهتر و تمیزتر کد است.

نحوه عملکرد:

1. تعریف رابط استراتژی (Strategy Interface) با یک رفتار مشترک به نام execute برای اعمال معنایی.
2. ایجاد کلاس‌های استراتژی (Concrete Strategy Classes) که هر کدام یک نوع عملیات معنایی را پیاده‌سازی می‌کنند.
3. نگهداری نقشه‌ای از کدها در کلاس CodeGenerator که هر کد به یک عمل معنایی خاص ارجاع دارد.
4. استفاده از استراتژی‌ها: در متد semanticFunction به‌صورت خودکار عمل معنایی مرتبط پیدا و اجرا می‌شود.

این روش باعث خواناتر شدن و قابل نگهداری‌تر شدن کد می‌شود.

```java
‍‍‍public interface SemanticAction {
    void execute(CodeGenerator context, Token next);
}
```
```java
‍‍‍public class PrintAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.print();
    }
}
```
```java
‍‍‍public class SubAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.sub();
    }
}
```
```java
‍‍‍public class AssignAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.assign();
    }
}
```
```java
‍‍‍public class AddAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.add();
    }
}
```
### مزایای این Refactoring

- **حذف بلوک‌های شرطی بزرگ:** 
   دستورات شرطی (مانند `switch` یا `if-else`) که باعث پیچیدگی کد می‌شدند با یک الگوی استراتژی ساده و قابل توسعه جایگزین شده است.

- **اصل باز/بسته (Open/Closed Principle):** 
   در این روش، افزودن اعمال معنایی جدید فقط به ایجاد کلاس جدید و افزودن آن به نقشه نیاز دارد. بدون نیاز به تغییرات در کدهای موجود، می‌توان به راحتی عملکردهای جدیدی به سیستم اضافه کرد.

- **افزایش خوانایی کد:** 
   هر عمل معنایی در کلاس خود محصور می‌شود و این باعث می‌شود کد به‌راحتی قابل فهم‌تر و سازمان‌دهی‌شده‌تر باشد.

- **قابلیت استفاده مجدد:** 
   کلاس‌های استراتژی می‌توانند در بخش‌های مختلف سیستم مورد استفاده قرار بگیرند، بدون اینکه نیاز به کپی‌کردن کد باشد.

- **نگهداری و گسترش آسان‌تر:** 
   تغییرات در نحوه پیاده‌سازی هر عمل معنایی به راحتی در کلاس استراتژی مربوطه انجام می‌شود. این تغییرات تأثیری در سایر بخش‌های کد نخواهند داشت و نگهداری سیستم ساده‌تر می‌شود.
### نتیجه‌گیری

استفاده از الگوی استراتژی (یا پلی‌مورفیسم به جای شرط‌ها) باعث شده که کد ما از بلوک‌های شرطی پیچیده پاک شود و به یک ساختار ساده‌تر و قابل توسعه تبدیل شود. این تغییر باعث افزایش خوانایی، قابلیت نگهداری و انعطاف‌پذیری سیستم می‌شود و همچنین امکان افزودن ویژگی‌های جدید بدون تغییر در ساختار موجود را فراهم می‌کند.



# جداسازی پرسش از تغییرات (Separate Query from Modifier)

## مقدمه
الگوی جداسازی پرسش از تغییرات به معنای جدا کردن عملیات تغییر وضعیت یک شی از عملیات فقط خواندن اطلاعات آن است. این الگو باعث کد واضح‌تر و تمیزتر و جلوگیری از رفتار غیرمنتظره در برنامه می‌شود.

## نحوه عملکرد
1. **عملکرد پرسش (Query):**  
   متدهایی که فقط اطلاعات را بدون تغییر وضعیت برمی‌گردانند. این متدها معمولاً مقدار فیلدها یا ویژگی‌های شی را بدون هیچ تغییر یا تأثیر بازمی‌گردانند.

2. **عملکرد تغییر (Modifier):**  
   متدهایی که وضعیت شی را تغییر می‌دهند و ممکن است ویژگی‌های آن را به‌روزرسانی یا عملیات دیگر روی داده‌ها انجام دهند.

3. **جدا کردن دو نوع متد:**  
   متدهای پرسش از متدهای تغییر در وضعیت شی جدا می‌شوند تا از بروز اشتباهات ناشی از تغییر وضعیت در حین پرسش جلوگیری شود.

## مزایای این Refactoring
1. **کاهش اشتباهات:**  
   با جدا کردن پرسش از تغییرات، احتمال بروز اشتباهات تصادفی در تغییر وضعیت کمتر می‌شود.

2. **کد تمیزتر و قابل خواندن‌تر:**  
   متدهای پرسش و تغییر وضعیت واضح و متمایز بوده و هرکدام وظایف خاص خود را دارند که فهم کد را ساده‌تر می‌کند.

3. **سادگی تست‌ها:**  
   تست واحد (unit tests) برای پرسش و تغییر جداگانه و ساده‌تر می‌شود.

4. **افزایش انعطاف‌پذیری:**  
   سیستم انعطاف‌پذیرتر شده و تغییر وضعیت بدون تأثیر منفی بر بخش‌های دیگر کد قابل انجام است.


ما این بخش را در کد:
```java
‍‍‍public void semanticFunction(int func, Token next) {
        SemanticAction action = semanticActionsMap.get(func);
        if (action != null) {
            action.execute(this, next);
        } else {
            throw new IllegalArgumentException("Undefined semantic action: " + func);
        }
    }
```

به query و modifier تقسیم کردیم:

```java
‍‍‍public void performSemanticAction(int func, Token next) {
        if (isActionDefined(func)) {
            SemanticAction action = getSemanticAction(func);
            action.execute(this, next);
        } else {
            throw new IllegalArgumentException("Undefined semantic action: " + func);
        }
    }
```
```java
‍‍‍public void printMemory() {
        memory.pintCodeBlock();
    }
```
```java
public boolean isActionDefined(int func) {
    return semanticActionsMap.containsKey(func);
}

public SemanticAction getSemanticAction(int func) {
    return semanticActionsMap.get(func);
    }
```

# فیلد محصور شده (Self Encapsulated Field)

## مقدمه
الگوی Self Encapsulated Field یکی از تکنیک‌های Refactoring است که به جای دسترسی مستقیم به فیلدهای کلاس، دسترسی از طریق متدهای getter و setter انجام می‌شود تا کنترل بیشتر و انعطاف‌پذیری بهتری روی داده‌های کلاس فراهم شود.

## نحوه عملکرد
1. **دسترسی به فیلدها از طریق متدها:**  
   به جای استفاده مستقیم از فیلدها، دسترسی به آنها از طریق متدهای getter و setter انجام می‌شود و فیلدها به صورت محصور در کلاس باقی می‌مانند.

2. **محدود کردن دسترسی به فیلدها:**  
   متدهای getter و setter می‌توانند شامل منطق اضافی برای اعتبارسنجی و کنترل تغییرات باشند؛ مثلاً قبل از اعمال تغییرات داده‌ها را بررسی کنند.

3. **ساده‌سازی تغییرات در آینده:**  
   وقتی به فیلدها از طریق متدها دسترسی می‌شود، می‌توان بدون تغییر سایر بخش‌های کد، نحوه دسترسی یا نوع داده فیلد را تغییر داد.

## مزایای این Refactoring
1. **کنترل بیشتر:**  
   با استفاده از getter و setter، کنترل بهتری روی نحوه دسترسی و تغییر داده‌ها وجود دارد و تغییرات کنترل‌شده‌تر انجام می‌شود.

2. **افزایش انعطاف‌پذیری:**  
   امکان افزودن منطق خاص در زمان دریافت یا تغییر مقدار فیلدها فراهم می‌شود، بدون نیاز به تغییر در سایر بخش‌های کد.

3. **افزایش خوانایی و نگهداری آسان‌تر:**  
   کد واضح‌تر و تمیزتر شده و نگهداری و توسعه آن آسان‌تر می‌گردد.

4. **حفاظت از داده‌ها:**  
   فیلدهایی که به‌طور مستقیم قابل دسترسی نیستند، از تغییرات نادرست محافظت می‌شوند و فقط تغییرات معتبر و منطقی روی داده‌ها اعمال می‌شود.

```java
public class Symbol {

    // Private fields to ensure encapsulation
    private SymbolType type;
    private int address;

    // Constructor to initialize the fields
    public Symbol(SymbolType type, int address) {
        this.type = type;
        this.address = address;
    }

    // Getter for type
    public SymbolType getType() {
        return type;
    }

    // Setter for type
    public void setType(SymbolType type) {
        // Optionally, you could add validation here to control how the type is set
        this.type = type;
    }

    // Getter for address
    public int getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(int address) {
        // Optionally, you could add validation to ensure a valid address is set
        this.address = address;
    }
}
```

**نتیجه‌گیری**

الگوی Self Encapsulated Field به شما این امکان را می‌دهد که دسترسی به فیلدهای کلاس را از طریق متدهای خاص و کنترل‌شده مدیریت کنید. این امر باعث می‌شود که تغییرات در نحوه دسترسی به داده‌ها ساده‌تر شود، کد خواناتر و نگهداری آن آسان‌تر گردد. در کل، این تکنیک به‌ویژه در پروژه‌های بزرگ و پیچیده می‌تواند به بهبود کیفیت کد و انعطاف‌پذیری آن کمک کند.

# انتقال متد (Moving Method)

## مقدمه
الگوی انتقال متد زمانی استفاده می‌شود که متدی در یک کلاس قرار دارد ولی بیشتر مرتبط با کلاس دیگری است. در این حالت، متد از کلاسی که در آن قرار دارد به کلاس دیگر منتقل می‌شود تا عملکرد سیستم بهتر و قابل فهم‌تر شود.

## نحوه عملکرد
در این الگو، متدهایی که به کلاس دیگری منتقل می‌شوند معمولاً ویژگی‌ها یا داده‌هایی دارند که به کلاس مقصد مرتبط‌تر هستند. این کار باعث می‌شود کلاس‌ها تمرکز بیشتری روی وظایف خود داشته باشند و وابستگی‌ها به دیگر کلاس‌ها کاهش یابد.

## مزایای استفاده از انتقال متد
1. **کاهش پیچیدگی:**  
   با انتقال متد به کلاسی که بیشتر به آن مرتبط است، پیچیدگی کلاس‌ها کاهش می‌یابد و هر کلاس تنها مسئولیت‌های خاص خود را بر عهده می‌گیرد.

2. **افزایش خوانایی:**  
   کد خواناتر می‌شود چون متدها در کلاسی قرار می‌گیرند که انتظار می‌رود آن‌ها را داشته باشد.

3. **کاهش وابستگی‌ها:**  
   وابستگی‌ها بین کلاس‌ها کاهش یافته و طراحی نرم‌افزار تمیزتر و ساده‌تر می‌شود.

## در پروژه
برای مثال در کد پروژه، توابعی که بهتر است از کلاس `klass` خارج باشند را به کلاس دیگر منتقل کردیم، مانند توابع مربوط به `symbolTable`.

```java
public void addMethodParameter(String methodName, String parameterName) {
    Method method = Methodes.get(methodName);
    if (method != null) {
        method.addParameter(parameterName);
    }
}

public void addMethod(String methodName, int address, SymbolType returnType) {
    if (Methodes.containsKey(methodName)) {
        ErrorHandler.printError("This method already defined");
    }
    Methodes.put(methodName, new Method(address, returnType));
}
```
کدهای مربوط به این بخش با کامیت های قبلی تداخل پیدا کرده.
</div>
