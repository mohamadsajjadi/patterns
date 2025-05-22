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

<h4>نمونه کد</h4>
```java
public class ParseFacade {
    private Parser parser;

    public ParseFacade() {
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

<h3>نتیجه‌گیری نهایی</h3>
<ul>
  <li>✅ پیچیدگی‌های سیستم به‌طور مؤثر مدیریت شد.</li>
  <li>✅ کد مشتری ساده‌تر و با قابلیت نگهداری بالاتر طراحی شد.</li>
  <li>✅ قابلیت توسعه و انعطاف‌پذیری سیستم بهبود یافت.</li>
</ul>

</div>
