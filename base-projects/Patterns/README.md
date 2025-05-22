<div dir="rtl">

# آزمایش شماره V (بازآرایی) - اعمال الگوی Facade

## هدف
ساده‌سازی فرآیند راه‌اندازی و تعامل با کامپونت‌های پیچیده در سیستم‌های تجزیه‌گر (Parser) با استفاده از الگوی Facade.

## مشکل
- تعامل مستقیم با چندین کامپونت مانند `CodeGenerator`، `ParseTable` و `Rule` نیازمند راه‌اندازی و مدیریت جداگانه است.
- این رویکرد باعث پیچیدگی کد مشتری، وابستگی زیاد به جزئیات داخلی، و کاهش خوانایی و قابلیت نگهداری می‌شود.

## راه‌حل
ایجاد کلاس `ParserFacade` برای:
- پنهان کردن پیچیدگی‌های راه‌اندازی کامپونت‌ها.
- ارائه یک رابط ساده و یکپارچه به مشتری.

### نمونه کد
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
</div>
