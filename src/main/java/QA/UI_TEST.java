package QA;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;


import java.time.Duration;
import java.util.List;

public class UI_TEST {

    public class JobPageTests {
        private WebDriver driver;
        private WebDriverWait wait;
        private JavascriptExecutor jsExecutor;


        @BeforeClass
        public void setUp() {
            driver = new ChromeDriver();
            jsExecutor = (JavascriptExecutor) driver;
            wait = new WebDriverWait(driver, Duration.ofSeconds(2000));
            driver.get("https://rakaya.sa/jobs/job-application");  // Replace with the actual URL
        }
        private void clickSubmitButton() {
            WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@type='submit' and(div='إرسال')])")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", submitButton);  // انتظر حتى يختفي زر الإرسال أو تتحرك الصفحة
        }
        private boolean isErrorMessageDisplayed(String errorMessage) {
            try {
                WebElement error = driver.findElement(By.xpath("//div[@class='text-danger' and contains(text(), '" + errorMessage + "')]"));
                return error.isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        //  Test Case 1: Verify Arabic characters are accepted
        @Test
        public void ate1stArabicCharactersAccepted() throws InterruptedException {
            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='name']")));
            nameField.sendKeys("بشرى عبد الخالق عليثه الحربي");
            Thread.sleep(200);
            clickSubmitButton();
            boolean isErrorMessagePresent = isElementPresent(driver, By.xpath("//div[@class='text-danger']"));
            Thread.sleep(2000);
            Assert.assertTrue(isErrorMessagePresent);
            Thread.sleep(2000);


        }

        public static boolean isElementPresent(WebDriver driver, By by) {
            try {
                driver.findElement(by);
                return true;  // العنصر موجود
            } catch (NoSuchElementException e) {
                return false;  // العنصر غير موجود
            }
        }


//            String alert= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'الاسم')]"))).getText();
//            Assert.assertEquals( alert,"الاسم الكامل مطلوب");


        //      Test Case 2: Verify special characters are rejected
        @Test
        public void ate2stSpecialCharactersRejected() {
            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='name']")));
            nameField.sendKeys("بشرى عبد الخالق،");

//           WebElement alert = driver.findElement(By.xpath("(//*[contains(text(), 'الاسم')])[2]"));
//            Assert.assertEquals( alert,"الاسم الكامل مطلوب");
            Assert.assertTrue(isErrorMessageDisplayed("الاسم الكامل مطلوب"), "No error displayed for name with special characters.");
            clickSubmitButton();

        }

        // Test Case 3: Verify English characters are rejected
        @Test
        public void ate3stEnglishCharactersRejected() {
            WebElement nameField = driver.findElement(By.xpath("//input[@name='name']"));  // Replace with the actual ID of the name field
            nameField.sendKeys(Keys.COMMAND + "a");
            nameField.sendKeys(Keys.BACK_SPACE);
            nameField.sendKeys("bushra alharbi");
            String alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'الاسم')]"))).getText();
            Assert.assertEquals(alert, "الاسم الكامل مطلوب ");


        }

        //        // Test Case 4: Verify empty input is not accepted
        @Test
        public void ate4stEmptyInputRejected() throws Exception {

            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='name']")));
            nameField.sendKeys(Keys.COMMAND + "a");
            nameField.sendKeys(Keys.BACK_SPACE);
            nameField.sendKeys("");// ترك الحقل فارغًا
            Thread.sleep(2000);
            clickSubmitButton();
            String alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'الاسم')]"))).getText();
            Assert.assertEquals(alert, "الاسم الكامل مطلوب");
        }
//
        @Test
        public void bte1stEmail() throws InterruptedException {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys("example@example.com");
           clickSubmitButton();
            Assert.assertFalse(isErrorMessageDisplayed("البريد الإلكتروني مطلوب"), "Error displayed for valid email.");

            Thread.sleep(500);

        }

        @Test
        public void bte2stEmail() {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys(Keys.COMMAND + "a");
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys("بريد@إلكتروني.كوم");
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("يجب أن يكون بريدًا إلكترونيًا صالحا"), "No error displayed for invalid Arabic email.");
        }

        @Test
        public void bte3stEmail() {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys(Keys.COMMAND + "a");
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys("example@!example.com");
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("يجب أن يكون بريدًا إلكترونيًا صالحًا\n"), "No error displayed for invalid special characters email.");

        }

        @Test
        public void bte4stEmail() {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys(Keys.COMMAND + "a");
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys("exampleexample.com");
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("يجب أن يكون بريدًا إلكترونيًا صالحا"), "No error displayed for email missing '@'.");


        }

        @Test
        public void bte5stEmail() {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys(Keys.COMMAND + "a");
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys("example@examplecom");
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("يجب أن يكون بريدًا إلكترونيًا صالحا"), "No error displayed for email missing '.'.");

        }

        @Test
        public void btes6tEmail() throws Exception {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
            emailField.sendKeys(Keys.COMMAND + "a");
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys("");// ترك الحقل فارغًا
            Thread.sleep(2000);
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("البريد الإلكتروني مطلوب"), "Empity");

        }
@Test
        public void ctest1Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']")); // استبدل بـ ID أو XPath
            phoneNumberField.sendKeys("509897843");
          clickSubmitButton();
            Assert.assertFalse(isErrorMessageDisplayed("رقم الجوال مطلوب "), "Error displayed for valid phone number.");
        }

        @Test
        public void ctest2Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            phoneNumberField.sendKeys(Keys.COMMAND + "a");
            phoneNumberField.sendKeys(Keys.BACK_SPACE);
            phoneNumberField.sendKeys("454327621");  // أدخل رقم يبدأ بـ4
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("رقم الجوال مطلوب"), "No error displayed for invalid start number.");
        }

        @Test
        public void ctest3Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            phoneNumberField.sendKeys(Keys.COMMAND + "a");
            phoneNumberField.sendKeys(Keys.BACK_SPACE);
            phoneNumberField.sendKeys("50878352");  // أدخل رقم قصير
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("رقم الجوال مطلوب"), "No error displayed for too short number.");
        }

        @Test
        public void ctest4Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            phoneNumberField.sendKeys(Keys.COMMAND + "a");
            phoneNumberField.sendKeys(Keys.BACK_SPACE);
            phoneNumberField.sendKeys("509897943");  // أدخل رقم طويل
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("رقم الجوال مطلوب"), "No error displayed for too long number.");
        }

        @Test
        public void ctest5Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            phoneNumberField.sendKeys(Keys.COMMAND + "a");
            phoneNumberField.sendKeys(Keys.BACK_SPACE);
            phoneNumberField.sendKeys("5000-8786");  // أدخل حروف أو رموز خاصة
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("رقم الجوال مطلوب"), "No error displayed for letters or special characters.");
        }


        @Test
        public void ctest6Phone() {
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            phoneNumberField.sendKeys(Keys.COMMAND + "a");
            phoneNumberField.sendKeys(Keys.BACK_SPACE);
            clickSubmitButton();
            Assert.assertTrue(isErrorMessageDisplayed("رقم الجوال مطلوب"), "No error displayed for empty field.");
        }

        private void clickElementUsingJavaScript(WebElement element) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click();", element);
        }

        // اختبار اختيار "مواطن"
        @Test
        public void testSelectCitizenOption() {
            WebElement citizenRadioButton = driver.findElement(By.id("resident_status0"));
            clickElementUsingJavaScript(citizenRadioButton);
            Assert.assertTrue(citizenRadioButton.isSelected(), "مواطن option should be selected.");
        }

        // اختبار اختيار "مقيم"
        @Test
        public void testSelectResidentOption() {
            WebElement residentRadioButton = driver.findElement(By.id("resident_status1"));
            clickElementUsingJavaScript(residentRadioButton);
            Assert.assertTrue(residentRadioButton.isSelected(), "مقيم option should be selected.");
        }

        // اختبار اختيار "زائر"
        @Test
        public void testSelectVisitorOption() {
            WebElement visitorRadioButton = driver.findElement(By.id("resident_status2"));
            clickElementUsingJavaScript(visitorRadioButton);
            Assert.assertTrue(visitorRadioButton.isSelected(), "زائر option should be selected.");
        }

        // اختبار اختيار "أخرى"
        @Test
        public void testSelectOtherOption() {
            WebElement otherRadioButton = driver.findElement(By.id("resident_status3"));
            clickElementUsingJavaScript(otherRadioButton);
            Assert.assertTrue(otherRadioButton.isSelected(), "أخرى option should be selected.");
        }

        // اختبار فقط أن خيار واحد يمكن أن يكون مختارًا في نفس الوقت
        @Test
        public void testOnlyOneOptionSelected() {
            WebElement citizenRadioButton = driver.findElement(By.id("resident_status0"));
            WebElement residentRadioButton = driver.findElement(By.id("resident_status1"));
            WebElement visitorRadioButton = driver.findElement(By.id("resident_status2"));
            WebElement otherRadioButton = driver.findElement(By.id("resident_status3"));

            // تحقق من أنه لا يمكن تحديد أكثر من خيار واحد في نفس الوقت
            clickElementUsingJavaScript(citizenRadioButton);
            Assert.assertTrue(citizenRadioButton.isSelected(), "مواطن option should be selected");

            clickElementUsingJavaScript(residentRadioButton);
            Assert.assertTrue(residentRadioButton.isSelected(), "مقيم option should be selected");
            Assert.assertFalse(citizenRadioButton.isSelected(), "مواطن option should be deselected");

            clickElementUsingJavaScript(visitorRadioButton);
            Assert.assertTrue(visitorRadioButton.isSelected(), "زائر option should be selected");
            Assert.assertFalse(residentRadioButton.isSelected(), "مقيم option should be deselected");

            clickElementUsingJavaScript(otherRadioButton);
            Assert.assertTrue(otherRadioButton.isSelected(), "أخرى option should be selected");
            Assert.assertFalse(visitorRadioButton.isSelected(), "زائر option should be deselected");
        }

        // اختبار أنه لا يوجد خيار افتراضي محدد
        @Test
        public void testNoDefaultOptionSelected() {
            WebElement citizenRadioButton = driver.findElement(By.id("resident_status0"));
            WebElement residentRadioButton = driver.findElement(By.id("resident_status1"));
            WebElement visitorRadioButton = driver.findElement(By.id("resident_status2"));
            WebElement otherRadioButton = driver.findElement(By.id("resident_status3"));

            // تحقق من أنه لا يوجد خيار مختار بشكل افتراضي
            Assert.assertFalse(citizenRadioButton.isSelected(), "مواطن option should not be selected by default.");
            Assert.assertFalse(residentRadioButton.isSelected(), "مقيم option should not be selected by default.");
            Assert.assertFalse(visitorRadioButton.isSelected(), "زائر option should not be selected by default.");
            Assert.assertFalse(otherRadioButton.isSelected(), "أخرى option should not be selected by default.");
        }

        // اختبار أن رسالة الخطأ تظهر عند تقديم النموذج بدون اختيار أي خيار
        @Test
        public void testErrorMessageWhenNoOptionSelected() {
            // لا تحدد أي خيار ثم قدم النموذج
            clickSubmitButton();

            // تحقق من ظهور رسالة الخطأ
            Assert.assertTrue(isErrorMessageDisplayed("حالة الإقامة مطلوبة"), "Error message should appear when no option is selected");
        }
        @Test
        public void testPhoneFieldHiddenWhenOtherOptionSelected() {
            WebElement otherRadioButton = driver.findElement(By.id("resident_status3"));
            clickElementUsingJavaScript(otherRadioButton);

            WebElement phoneNumberField = driver.findElement(By.xpath("//input[@name='phone']"));
            Assert.assertFalse(phoneNumberField.isDisplayed(), "Phone number field should be hidden when 'أخرى' is selected");
        }

        @Test
        public void testGenderOptionsDisplayed() {
            WebElement maleRadioButton = driver.findElement(By.id("gender0"));
            WebElement femaleRadioButton = driver.findElement(By.id("gender1"));

            // تحقق من أن كلا الخيارين "ذكر" و "أنثى" يظهران في النموذج
            Assert.assertTrue(maleRadioButton.isDisplayed(), "ذكر option should be displayed.");
            Assert.assertTrue(femaleRadioButton.isDisplayed(), "أنثى option should be displayed.");
        }

        // اختبار اختيار "ذكر"
        @Test
        public void testSelectMaleOption() {
            WebElement maleRadioButton = driver.findElement(By.id("gender0"));
            clickElementUsingJavaScript(maleRadioButton);
            Assert.assertTrue(maleRadioButton.isSelected(), "ذكر option should be selected.");
        }

        // اختبار اختيار "أنثى"
        @Test
        public void testSelectFemaleOption() {
            WebElement femaleRadioButton = driver.findElement(By.id("gender1"));
            clickElementUsingJavaScript(femaleRadioButton);
            Assert.assertTrue(femaleRadioButton.isSelected(), "أنثى option should be selected.");
        }

        // اختبار أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
        @Test
        public void testOnlyOneGenderOptionSelected() {
            WebElement maleRadioButton = driver.findElement(By.id("gender0"));
            WebElement femaleRadioButton = driver.findElement(By.id("gender1"));

            // تحقق من أنه لا يمكن تحديد أكثر من خيار في نفس الوقت
            clickElementUsingJavaScript(maleRadioButton);
            Assert.assertTrue(maleRadioButton.isSelected(), "ذكر option should be selected");

            clickElementUsingJavaScript(femaleRadioButton);
            Assert.assertTrue(femaleRadioButton.isSelected(), "أنثى option should be selected");
            Assert.assertFalse(maleRadioButton.isSelected(), "ذكر option should be deselected");
        }

        // اختبار أنه لا يوجد خيار افتراضي محدد
        @Test
        public void testNoDefaultGenderOptionSelected() {
            WebElement maleRadioButton = driver.findElement(By.id("gender0"));
            WebElement femaleRadioButton = driver.findElement(By.id("gender1"));

            // تحقق من أنه لا يوجد خيار مختار بشكل افتراضي
            Assert.assertFalse(maleRadioButton.isSelected(), "ذكر option should not be selected by default.");
            Assert.assertFalse(femaleRadioButton.isSelected(), "أنثى option should not be selected by default.");
        }

        // اختبار ظهور رسالة الخطأ إذا تم إرسال النموذج بدون اختيار الجنس
        @Test
        public void testErrorMessageWhenNoGenderSelected() {
            // لا تحدد أي خيار ثم قدم النموذج
            clickSubmitButton();

            // تحقق من ظهور رسالة الخطأ
            Assert.assertTrue(isErrorMessageDisplayed("الجنس مطلوب"), "Error message should appear when no gender is selected");
        }
        @Test
        public void testMaritalStatusOptionsDisplayed() {
            WebElement singleRadioButton = driver.findElement(By.id("marital_status0"));
            WebElement marriedRadioButton = driver.findElement(By.id("marital_status1"));
            WebElement divorcedRadioButton = driver.findElement(By.id("marital_status2"));
            WebElement widowedRadioButton = driver.findElement(By.id("marital_status3"));
            WebElement otherRadioButton = driver.findElement(By.id("marital_status4"));

            // تحقق من أن كل الخيارات تظهر في النموذج
            Assert.assertTrue(singleRadioButton.isDisplayed(), "أعزب/عزباء option should be displayed.");
            Assert.assertTrue(marriedRadioButton.isDisplayed(), "متزوج/ة option should be displayed.");
            Assert.assertTrue(divorcedRadioButton.isDisplayed(), "مطلق/ة option should be displayed.");
            Assert.assertTrue(widowedRadioButton.isDisplayed(), "أرمل/ة option should be displayed.");
            Assert.assertTrue(otherRadioButton.isDisplayed(), "أخرى option should be displayed.");
        }

        // اختبار اختيار "أعزب/عزباء"
        @Test
        public void testSelectSingleOption() {
            WebElement singleRadioButton = driver.findElement(By.id("marital_status0"));
            clickElementUsingJavaScript(singleRadioButton);
            Assert.assertTrue(singleRadioButton.isSelected(), "أعزب/عزباء option should be selected.");
        }

        // اختبار اختيار "متزوج/ة"
        @Test
        public void testSelectMarriedOption() {
            WebElement marriedRadioButton = driver.findElement(By.id("marital_status1"));
            clickElementUsingJavaScript(marriedRadioButton);
            Assert.assertTrue(marriedRadioButton.isSelected(), "متزوج/ة option should be selected.");
        }

        // اختبار اختيار "مطلق/ة"
        @Test
        public void testSelectDivorcedOption() {
            WebElement divorcedRadioButton = driver.findElement(By.id("marital_status2"));
            clickElementUsingJavaScript(divorcedRadioButton);
            Assert.assertTrue(divorcedRadioButton.isSelected(), "مطلق/ة option should be selected.");
        }

        // اختبار اختيار "أرمل/ة"
        @Test
        public void testSelectWidowedOption() {
            WebElement widowedRadioButton = driver.findElement(By.id("marital_status3"));
            clickElementUsingJavaScript(widowedRadioButton);
            Assert.assertTrue(widowedRadioButton.isSelected(), "أرمل/ة option should be selected.");
        }

        // اختبار اختيار "أخرى"
        @Test
        public void testSelectOtherOption1() {
            WebElement otherRadioButton = driver.findElement(By.id("marital_status4"));
            clickElementUsingJavaScript(otherRadioButton);
            Assert.assertTrue(otherRadioButton.isSelected(), "أخرى option should be selected.");
        }

        // اختبار أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
        @Test
        public void testOnlyOneOptionSelected2() {
            WebElement singleRadioButton = driver.findElement(By.id("marital_status0"));
            WebElement marriedRadioButton = driver.findElement(By.id("marital_status1"));
            WebElement divorcedRadioButton = driver.findElement(By.id("marital_status2"));
            WebElement widowedRadioButton = driver.findElement(By.id("marital_status3"));
            WebElement otherRadioButton = driver.findElement(By.id("marital_status4"));

            // تحقق من أنه لا يمكن تحديد أكثر من خيار في نفس الوقت
            clickElementUsingJavaScript(singleRadioButton);
            Assert.assertTrue(singleRadioButton.isSelected(), "أعزب/عزباء option should be selected");

            clickElementUsingJavaScript(marriedRadioButton);
            Assert.assertTrue(marriedRadioButton.isSelected(), "متزوج/ة option should be selected");
            Assert.assertFalse(singleRadioButton.isSelected(), "أعزب/عزباء option should be deselected");

            clickElementUsingJavaScript(divorcedRadioButton);
            Assert.assertTrue(divorcedRadioButton.isSelected(), "مطلق/ة option should be selected");
            Assert.assertFalse(marriedRadioButton.isSelected(), "متزوج/ة option should be deselected");

            clickElementUsingJavaScript(widowedRadioButton);
            Assert.assertTrue(widowedRadioButton.isSelected(), "أرمل/ة option should be selected");
            Assert.assertFalse(divorcedRadioButton.isSelected(), "مطلق/ة option should be deselected");

            clickElementUsingJavaScript(otherRadioButton);
            Assert.assertTrue(otherRadioButton.isSelected(), "أخرى option should be selected");
            Assert.assertFalse(widowedRadioButton.isSelected(), "أرمل/ة option should be deselected");
        }

        // اختبار أنه لا يوجد خيار افتراضي محدد
        @Test
        public void testNoDefaultOptionSelected3() {
            WebElement singleRadioButton = driver.findElement(By.id("marital_status0"));
            WebElement marriedRadioButton = driver.findElement(By.id("marital_status1"));
            WebElement divorcedRadioButton = driver.findElement(By.id("marital_status2"));
            WebElement widowedRadioButton = driver.findElement(By.id("marital_status3"));
            WebElement otherRadioButton = driver.findElement(By.id("marital_status4"));

            // تحقق من أنه لا يوجد خيار مختار بشكل افتراضي
            Assert.assertFalse(singleRadioButton.isSelected(), "أعزب/عزباء option should not be selected by default.");
            Assert.assertFalse(marriedRadioButton.isSelected(), "متزوج/ة option should not be selected by default.");
            Assert.assertFalse(divorcedRadioButton.isSelected(), "مطلق/ة option should not be selected by default.");
            Assert.assertFalse(widowedRadioButton.isSelected(), "أرمل/ة option should not be selected by default.");
            Assert.assertFalse(otherRadioButton.isSelected(), "أخرى option should not be selected by default.");
        }

        // اختبار ظهور رسالة الخطأ عند تقديم النموذج بدون اختيار الحالة الاجتماعية
        @Test
        public void testErrorMessageWhenNoOptionSelected4() {
            // لا تحدد أي خيار ثم قدم النموذج
            clickSubmitButton();

            // تحقق من ظهور رسالة الخطأ
            Assert.assertTrue(isErrorMessageDisplayed("الحالة الاجتماعية مطلوبة"), "Error message should appear when no marital status option is selected.");
        }
        // اختبار ظهور خيار "نعم" و "لا"
        @Test
        public void testBothOptionsAreDisplayed() {
            WebElement yesOption = driver.findElement(By.id("has_relative0"));
            WebElement noOption = driver.findElement(By.id("has_relative1"));

            // تحقق من ظهور خيارات "نعم" و "لا"
            Assert.assertTrue(yesOption.isDisplayed(), "'نعم' option should be displayed.");
            Assert.assertTrue(noOption.isDisplayed(), "'لا' option should be displayed.");
        }

        // اختبار اختيار "نعم"
        @Test
        public void testSelectYesOption() {
            WebElement yesRadioButton = driver.findElement(By.id("has_relative0"));
            clickElementUsingJavaScript(yesRadioButton);

            // تحقق من أن خيار "نعم" تم اختياره
            Assert.assertTrue(yesRadioButton.isSelected(), "'نعم' option should be selected.");
        }

        // اختبار اختيار "لا"
        @Test
        public void testSelectNoOption() {
            WebElement noRadioButton = driver.findElement(By.id("has_relative1"));
            clickElementUsingJavaScript(noRadioButton);

            // تحقق من أن خيار "لا" تم اختياره
            Assert.assertTrue(noRadioButton.isSelected(), "'لا' option should be selected.");
        }

        // اختبار أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
        @Test
        public void testOnlyOneOptionSelected3() {
            WebElement yesRadioButton = driver.findElement(By.id("has_relative0"));
            WebElement noRadioButton = driver.findElement(By.id("has_relative1"));

            // اختر "نعم" ثم "لا" وتحقق من أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
            clickElementUsingJavaScript(yesRadioButton);
            Assert.assertTrue(yesRadioButton.isSelected(), "'نعم' option should be selected.");

            clickElementUsingJavaScript(noRadioButton);
            Assert.assertTrue(noRadioButton.isSelected(), "'لا' option should be selected.");
            Assert.assertFalse(yesRadioButton.isSelected(), "'نعم' option should be deselected.");
        }

        // اختبار أنه لا يوجد خيار مختار بشكل افتراضي
        @Test
        public void testNoDefaultOptionSelected4() {
            WebElement yesRadioButton = driver.findElement(By.id("has_relative0"));
            WebElement noRadioButton = driver.findElement(By.id("has_relative1"));

            // تحقق من أنه لا يوجد خيار مختار بشكل افتراضي
            Assert.assertFalse(yesRadioButton.isSelected(), "'نعم' option should not be selected by default.");
            Assert.assertFalse(noRadioButton.isSelected(), "'لا' option should not be selected by default.");
        }

        // اختبار ظهور رسالة خطأ عند تقديم النموذج دون اختيار خيار
        @Test
        public void testErrorMessageWhenNoOptionSelected3() {
            // لا تحدد أي خيار ثم قدم النموذج
            clickSubmitButton();

            // تحقق من ظهور رسالة الخطأ
            Assert.assertTrue(isErrorMessageDisplayed("هذا الحقل مطلوب"), "Error message should appear when no option is selected.");
        }
        @Test
        public void testBothOptionsAreDisplayed4() {
            WebElement yesOption = driver.findElement(By.id("previously_work_at_rakaya0"));
            WebElement noOption = driver.findElement(By.id("previously_work_at_rakaya1"));

            // تحقق من ظهور خيارات "نعم" و "لا"
            Assert.assertTrue(yesOption.isDisplayed(), "'نعم' option should be displayed.");
            Assert.assertTrue(noOption.isDisplayed(), "'لا' option should be displayed.");
        }

        // اختبار اختيار "نعم"
        @Test
        public void testSelectYesOption4() {
            WebElement yesRadioButton = driver.findElement(By.id("previously_work_at_rakaya0"));
            clickElementUsingJavaScript(yesRadioButton);

            // تحقق من أن خيار "نعم" تم اختياره
            Assert.assertTrue(yesRadioButton.isSelected(), "'نعم' option should be selected.");
        }

        // اختبار اختيار "لا"
        @Test
        public void testSelectNoOption4() {
            WebElement noRadioButton = driver.findElement(By.id("previously_work_at_rakaya1"));
            clickElementUsingJavaScript(noRadioButton);

            // تحقق من أن خيار "لا" تم اختياره
            Assert.assertTrue(noRadioButton.isSelected(), "'لا' option should be selected.");
        }

        // اختبار أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
        @Test
        public void testOnlyOneOptionSelected4() {
            WebElement yesRadioButton = driver.findElement(By.id("previously_work_at_rakaya0"));
            WebElement noRadioButton = driver.findElement(By.id("previously_work_at_rakaya1"));

            // اختر "نعم" ثم "لا" وتحقق من أنه لا يمكن اختيار أكثر من خيار في نفس الوقت
            clickElementUsingJavaScript(yesRadioButton);
            Assert.assertTrue(yesRadioButton.isSelected(), "'نعم' option should be selected.");

            clickElementUsingJavaScript(noRadioButton);
            Assert.assertTrue(noRadioButton.isSelected(), "'لا' option should be selected.");
            Assert.assertFalse(yesRadioButton.isSelected(), "'نعم' option should be deselected.");
        }

        // اختبار أنه لا يوجد خيار مختار بشكل افتراضي
        @Test
        public void testNoDefaultOptionSelected5() {
            WebElement yesRadioButton = driver.findElement(By.id("previously_work_at_rakaya0"));
            WebElement noRadioButton = driver.findElement(By.id("previously_work_at_rakaya1"));

            // تحقق من أنه لا يوجد خيار مختار بشكل افتراضي
            Assert.assertFalse(yesRadioButton.isSelected(), "'نعم' option should not be selected by default.");
            Assert.assertFalse(noRadioButton.isSelected(), "'لا' option should not be selected by default.");
        }

        // اختبار ظهور رسالة خطأ عند تقديم النموذج دون اختيار خيار
        @Test
        public void testErrorMessageWhenNoOptionSelected5() {
            // لا تحدد أي خيار ثم قدم النموذج
            clickSubmitButton();

            // تحقق من ظهور رسالة الخطأ
            Assert.assertTrue(isErrorMessageDisplayed("هذا الحقل مطلوب"), "Error message should appear when no option is selected.");
        }

        // دالة لمحاكاة النقر باستخدام Actions
        private void selectDepartmentUsingActions(WebElement element) {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
        }

        // دالة لمحاكاة النقر باستخدام JavascriptExecutor
        private void selectDepartmentUsingJavaScript(WebElement element) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click();", element);
        }
        @Test
        public void testJobTypeOptionsPresence() {
            WebElement fullTime = driver.findElement(By.xpath("//label[text()='دوام كامل']"));
            WebElement partTime = driver.findElement(By.xpath("//label[text()='دوام جزئي']"));
            WebElement remote = driver.findElement(By.xpath("//label[text()='عن بعد']"));
            WebElement flexible = driver.findElement(By.xpath("//label[text()='دوام مرن']"));
            WebElement seasonal = driver.findElement(By.xpath("//label[text()='موسمي']"));
            WebElement internship = driver.findElement(By.xpath("//label[text()='تدريب تعاوني /  تدريب صيفي']"));

            Assert.assertTrue(fullTime.isDisplayed(), "Option 'دوام كامل' should be visible");
            Assert.assertTrue(partTime.isDisplayed(), "Option 'دوام جزئي' should be visible");
            Assert.assertTrue(remote.isDisplayed(), "Option 'عن بعد' should be visible");
            Assert.assertTrue(flexible.isDisplayed(), "Option 'دوام مرن' should be visible");
            Assert.assertTrue(seasonal.isDisplayed(), "Option 'موسمي' should be visible");
            Assert.assertTrue(internship.isDisplayed(), "Option 'تدريب تعاوني / تدريب صيفي' should be visible");
        }
        private void selectOptionUsingJavaScript(WebElement element) {
            jsExecutor.executeScript("arguments[0].click();", element);
        }
        // اختبار تحديد كل خيار من قائمة "نوع الوظيفة"
        @Test
        public void testSelectFullTime() {
            WebElement fullTime = driver.findElement(By.xpath("//label[text()='دوام كامل']/preceding-sibling::input"));
            selectOptionUsingJavaScript(fullTime);
            Assert.assertTrue(fullTime.isSelected(), "Option 'دوام كامل' should be selected");
        }

        @Test
        public void testSelectPartTime() {
            WebElement partTime = driver.findElement(By.xpath("//label[text()='دوام جزئي']/preceding-sibling::input"));
            selectOptionUsingJavaScript(partTime);
            Assert.assertTrue(partTime.isSelected(), "Option 'دوام جزئي' should be selected");
        }

        @Test
        public void testSelectRemote() {
            WebElement remote = driver.findElement(By.xpath("//label[text()='عن بعد']/preceding-sibling::input"));
            selectOptionUsingJavaScript(remote);
            Assert.assertTrue(remote.isSelected(), "Option 'عن بعد' should be selected");
        }

        @Test
        public void testSelectFlexible() {
            WebElement flexible = driver.findElement(By.xpath("//label[text()='دوام مرن']/preceding-sibling::input"));
            selectOptionUsingJavaScript(flexible);
            Assert.assertTrue(flexible.isSelected(), "Option 'دوام مرن' should be selected");
        }

        @Test
        public void testSelectSeasonal() {
            WebElement seasonal = driver.findElement(By.xpath("//label[text()='موسمي']/preceding-sibling::input"));
            selectOptionUsingJavaScript(seasonal);
            Assert.assertTrue(seasonal.isSelected(), "Option 'موسمي' should be selected");
        }

        @Test
        public void testSelectInternship() {
            WebElement internship = driver.findElement(By.xpath("//label[text()='تدريب تعاوني /  تدريب صيفي']/preceding-sibling::input"));
            selectOptionUsingJavaScript(internship);
            Assert.assertTrue(internship.isSelected(), "Option 'تدريب تعاوني /  تدريب صيفي' should be selected");
//            WebElement experienceField = driver.findElement(By.id("experience_years_id")); // استبدل بالمعرف الصحيح
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));
//            Assert.assertFalse(experienceField.isDisplayed(), "حقل سنوات الخبرة يجب أن يختفي عند اختيار تدريب تعاوني / تدريب صيفي");
            Assert.assertFalse(salaryField.isDisplayed(), "حقل الراتب المتوقع يجب أن يختفي عند اختيار تدريب تعاوني / تدريب صيفي");
        }


        // اختبار التحقق من تحديد خيار واحد فقط في كل مرة
        @Test
        public void testOnlyOneJobTypeCanBeSelected() {
            WebElement fullTime = driver.findElement(By.xpath("//label[text()='دوام كامل']/preceding-sibling::input"));
            WebElement partTime = driver.findElement(By.xpath("//label[text()='دوام جزئي']/preceding-sibling::input"));

            selectOptionUsingJavaScript(fullTime);
            selectOptionUsingJavaScript(partTime);

            Assert.assertFalse(fullTime.isSelected(), "Only the latest selected option should be active");
            Assert.assertTrue(partTime.isSelected(), "Option 'دوام جزئي' should be selected");
        }
        public void testSalaryFieldAcceptsNumericOnly() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter numeric value (Arabic or English digits)
            salaryField.clear();
            salaryField.sendKeys("4000");

            // Verify the value in the salary field is numeric only
            Assert.assertEquals(salaryField.getAttribute("value"), "4000", "The salary field should accept numeric input.");
        }

        // Test case to verify salary field does not accept alphabets
        @Test
        public void testSalaryFieldRejectsAlphabets() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter alphabets in the salary field
            salaryField.clear();
            salaryField.sendKeys("abcd");

            // Verify that alphabets are rejected and not entered in the field
            Assert.assertNotEquals(salaryField.getAttribute("value"), "abcd", "The salary field should not accept alphabets.");
        }

        // Test case to verify salary field does not accept special characters
        @Test
        public void testSalaryFieldRejectsSpecialCharacters() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter special characters in the salary field
            salaryField.clear();
            salaryField.sendKeys("@#$%");

            // Verify that special characters are rejected and not entered in the field
            Assert.assertNotEquals(salaryField.getAttribute("value"), "@#$%", "The salary field should not accept special characters.");
        }

        // Test case to verify salary field does not accept values starting with 0
        @Test
        public void testSalaryFieldRejectsStartingWithZero() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter a value starting with 0 (e.g., 01234)
            salaryField.clear();
            salaryField.sendKeys("01234");

            // Verify the value is not accepted (i.e., the value should remain empty or reject the input)
            Assert.assertNotEquals(salaryField.getAttribute("value"), "01234", "The salary field should not accept values starting with 0.");
        }

        // Test case to verify salary field accepts values without starting with zero
        @Test
        public void testSalaryFieldRejectsZeroAsFirstDigit() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter a value like 0001
            salaryField.clear();
            salaryField.sendKeys("0001");

            // Verify that the salary field does not accept values starting with zero
            Assert.assertNotEquals(salaryField.getAttribute("value"), "0001", "The salary field should not accept values like 0001.");
        }

        // Test case to verify that salary input is within a valid range (e.g., should not accept negative values)
        @Test
        public void testSalaryFieldRejectsNegativeValues() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter a negative value in the salary field
            salaryField.clear();
            salaryField.sendKeys("-4000");

            // Verify the field does not accept negative values
            Assert.assertNotEquals(salaryField.getAttribute("value"), "-4000", "The salary field should not accept negative values.");
        }

        // Test case to verify that salary input is within the valid range of positive numbers
        @Test
        public void testSalaryFieldAcceptsPositiveValues() {
            WebElement salaryField = driver.findElement(By.name("salary_expectation"));

            // Enter a valid positive salary value
            salaryField.clear();
            salaryField.sendKeys("4000");

            // Verify the field accepts positive values
            Assert.assertEquals(salaryField.getAttribute("value"), "4000", "The salary field should accept positive numeric values.");
        }

        // Helper method to wait for element to be clickable



        // Helper method to wait for element to be clickable
        private void waitForElementToBeClickable(By by) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(by));
        }

        // Helper method to wait for element to be visible
        private void waitForElementToBeVisible(By by) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        }

        // Helper method to click an element using JavaScript
        private void clickUsingJavaScript(WebElement element) {
            jsExecutor.executeScript("arguments[0].click();", element);
        }

        // Test Case 1: Verify "جاهز حالًا" option can be selected
        @Test
        public void testSelectReadyNow() {
            WebElement readyNowOption = driver.findElement(By.id("availability_to_start0"));
            waitForElementToBeClickable(By.id("availability_to_start0"));

            // Try to click using standard click
            try {
                readyNowOption.click();
            } catch (ElementClickInterceptedException e) {
                // If standard click fails, use JavaScript to click
                clickUsingJavaScript(readyNowOption);
            }

            // Verify if the option is selected
            Assert.assertTrue(readyNowOption.isSelected(), "Option 'جاهز حالًا' should be selected.");
        }

        // Test Case 2: Verify "من أسبوعين إلى أربعة أسابيع" option can be selected
        @Test
        public void testSelectTwoToFourWeeks() {
            WebElement twoToFourWeeksOption = driver.findElement(By.id("availability_to_start1"));
            waitForElementToBeClickable(By.id("availability_to_start1"));

            // Try to click using standard click
            try {
                twoToFourWeeksOption.click();
            } catch (ElementClickInterceptedException e) {
                // If standard click fails, use JavaScript to click
                clickUsingJavaScript(twoToFourWeeksOption);
            }

            // Verify if the option is selected
            Assert.assertTrue(twoToFourWeeksOption.isSelected(), "Option 'من أسبوعين إلى أربعة أسابيع' should be selected.");
        }

        // Test Case 3: Verify "من أربعة أسابيع إلى ثمانية أسابيع" option can be selected
        @Test
        public void testSelectFourToEightWeeks() {
            WebElement fourToEightWeeksOption = driver.findElement(By.id("availability_to_start2"));
            waitForElementToBeClickable(By.id("availability_to_start2"));

            // Try to click using standard click
            try {
                fourToEightWeeksOption.click();
            } catch (ElementClickInterceptedException e) {
                // If standard click fails, use JavaScript to click
                clickUsingJavaScript(fourToEightWeeksOption);
            }

            // Verify if the option is selected
            Assert.assertTrue(fourToEightWeeksOption.isSelected(), "Option 'من أربعة أسابيع إلى ثمانية أسابيع' should be selected.");
        }

        // Test Case 4: Verify "أكثر من ثمانية أسابيع" option can be selected
        @Test
        public void testSelectMoreThanEightWeeks() {
            WebElement moreThanEightWeeksOption = driver.findElement(By.id("availability_to_start3"));
            waitForElementToBeClickable(By.id("availability_to_start3"));

            // Try to click using standard click
            try {
                moreThanEightWeeksOption.click();
            } catch (ElementClickInterceptedException e) {
                // If standard click fails, use JavaScript to click
                clickUsingJavaScript(moreThanEightWeeksOption);
            }

            // Verify if the option is selected
            Assert.assertTrue(moreThanEightWeeksOption.isSelected(), "Option 'أكثر من ثمانية أسابيع' should be selected.");
        }

        // Test Case 5: Verify only one option can be selected at a time
        @Test
        public void testOnlyOneOptionCanBeSelected() {
            WebElement readyNowOption = driver.findElement(By.id("availability_to_start0"));
            WebElement twoToFourWeeksOption = driver.findElement(By.id("availability_to_start1"));

            // Wait for the elements to be clickable
            waitForElementToBeClickable(By.id("availability_to_start0"));
            waitForElementToBeClickable(By.id("availability_to_start1"));

            // Click the first option
            readyNowOption.click();

            // Click the second option and verify the first option is unselected
            twoToFourWeeksOption.click();

            // Verify only the second option is selected
            Assert.assertTrue(twoToFourWeeksOption.isSelected(), "Option 'من أسبوعين إلى أربعة أسابيع' should be selected.");
            Assert.assertFalse(readyNowOption.isSelected(), "Option 'جاهز حالًا' should be unselected.");
        }


//        // اختبار للتأكد من وجود القائمة والخيارات
//        @Test
//        public void testDepartmentOptionsPresence() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            Assert.assertTrue(dropdown.isDisplayed(), "Department dropdown should be visible");
//
//            // تحقق من ظهور الخيارات المطلوبة
//            WebElement option1 = driver.findElement(By.xpath("//option[text()='اتقان الرقمية - تقنية المعلومات']"));
//            WebElement option2 = driver.findElement(By.xpath("//option[text()='جودة التشغيل']"));
//            WebElement option3 = driver.findElement(By.xpath("//option[text()='صحة وسلامة الغذاء']"));
//            WebElement option4 = driver.findElement(By.xpath("//option[text()='تسويق']"));
//            WebElement option5 = driver.findElement(By.xpath("//option[text()='علاقات عامة']"));
//            WebElement option6 = driver.findElement(By.xpath("//option[text()='موارد بشرية']"));
//
//            Assert.assertTrue(option1.isDisplayed(), "Option 'اتقان الرقمية - تقنية المعلومات' should be visible");
//            Assert.assertTrue(option2.isDisplayed(), "Option 'جودة التشغيل' should be visible");
//            Assert.assertTrue(option3.isDisplayed(), "Option 'صحة وسلامة الغذاء' should be visible");
//            Assert.assertTrue(option4.isDisplayed(), "Option 'تسويق' should be visible");
//            Assert.assertTrue(option5.isDisplayed(), "Option 'علاقات عامة' should be visible");
//            Assert.assertTrue(option6.isDisplayed(), "Option 'موارد بشرية' should be visible");
//        }
//
//        // اختبار لاختيار قسم "اتقان الرقمية - تقنية المعلومات"
//        @Test
//        public void testSelectEtqan() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionEtqan = driver.findElement(By.xpath("//option[text()='اتقان الرقمية - تقنية المعلومات']"));
//            selectDepartmentUsingActions(optionEtqan);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionEtqan.isSelected(), "Option 'اتقان الرقمية - تقنية المعلومات' should be selected");
//        }
//
//        // اختبار لاختيار قسم "جودة التشغيل"
//        @Test
//        public void testSelectQuality() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionQuality = driver.findElement(By.xpath("//option[text()='جودة التشغيل']"));
//            selectDepartmentUsingActions(optionQuality);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionQuality.isSelected(), "Option 'جودة التشغيل' should be selected");
//        }
//
//        // اختبار لاختيار قسم "صحة وسلامة الغذاء"
//        @Test
//        public void testSelectFoodSafety() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionFoodSafety = driver.findElement(By.xpath("//option[text()='صحة وسلامة الغذاء']"));
//            selectDepartmentUsingActions(optionFoodSafety);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionFoodSafety.isSelected(), "Option 'صحة وسلامة الغذاء' should be selected");
//        }
//
//        // اختبار لاختيار قسم "تسويق"
//        @Test
//        public void testSelectMarketing() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionMarketing = driver.findElement(By.xpath("//option[text()='تسويق']"));
//            selectDepartmentUsingActions(optionMarketing);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionMarketing.isSelected(), "Option 'تسويق' should be selected");
//        }
//
//        // اختبار لاختيار قسم "علاقات عامة"
//        @Test
//        public void testSelectPublicRelations() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionPublicRelations = driver.findElement(By.xpath("//option[text()='علاقات عامة']"));
//            selectDepartmentUsingActions(optionPublicRelations);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionPublicRelations.isSelected(), "Option 'علاقات عامة' should be selected");
//        }
//
//        // اختبار لاختيار قسم "موارد بشرية"
//        @Test
//        public void testSelectHR() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionHR = driver.findElement(By.xpath("//option[text()='موارد بشرية']"));
//            selectDepartmentUsingActions(optionHR);  // استخدم Actions للنقر
//
//            Assert.assertTrue(optionHR.isSelected(), "Option 'موارد بشرية' should be selected");
//        }
//
//        // اختبار للتأكد من أنه يمكن اختيار قسم واحد فقط
//        @Test
//        public void testOnlyOneOptionCanBeSelected() {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement dropdown = driver.findElement(By.xpath("//span[@id='react-select-6-live-region']"));
//            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
//            dropdown.click();
//
//            WebElement optionEtqan = driver.findElement(By.xpath("//option[text()='اتقان الرقمية - تقنية المعلومات']"));
//            selectDepartmentUsingActions(optionEtqan);  // استخدم Actions للنقر
//
//            WebElement optionQuality = driver.findElement(By.xpath("//option[text()='جودة التشغيل']"));
//            selectDepartmentUsingActions(optionQuality);  // استخدم Actions للنقر
//
//            Assert.assertFalse(optionEtqan.isSelected(), "Option 'اتقان الرقمية - تقنية المعلومات' should be deselected");
//            Assert.assertTrue(optionQuality.isSelected(), "Option 'جودة التشغيل' should be selected");
//        }


        }}


