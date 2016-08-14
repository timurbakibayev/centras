package com.gii.insreport;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Timur_hnimdvi on 07-Aug-16.
 */
public class FormTemplates {
    public static void applyTemplate(Form form, String fireBaseCatalog) {
        form.dateModified = new Date(); //ServerValue.TIMESTAMP;
        form.fireBaseCatalog = fireBaseCatalog;
        if (form.elements.size() == 0) { //this is the first load of the form after server initiation. Or manually created form.
            switch (fireBaseCatalog) {
                case ("preInsurance"):
                    ArrayList<Element> header = new ArrayList<>();
                    ArrayList<Element> personalInfo = new ArrayList<>();
                    ArrayList<Element> carInfo = new ArrayList<>();
                    ArrayList<Element> additionalInfo1 = new ArrayList<>();

                    form.elements.add(new Element(header, "header", "Заголовок"));

                    form.elements.add(new Element(personalInfo, "personalInfo", "Личные данные"));

                    form.elements.add(new Element(carInfo, "carInfo", "Данные по машине"));

                    form.elements.add(new Element(additionalInfo1, "additionalInfo1", "Дополнительные данные"));

                    header.add(new Element(Element.ElementType.eText, "city", "Город"));

                    header.add(new Element(Element.ElementType.eDate, "reportDate",
                            "Дата составления акта"));

                    header.add(new Element(Element.ElementType.eText, "employee",
                            "представитель АО «СК «Сентрас Иншуранс» - ФИО"));

                    header.add(new Element(Element.ElementType.eText, "client",
                            "клиента - ФИО"));

                    header.add(new Element(Element.ElementType.eText, "address",
                            "по адресу"));

                    personalInfo.add(new Element(Element.ElementType.eText, "owner", "Владелец"));

                    form.descriptionFields.add("owner");

                    personalInfo.add(new Element(Element.ElementType.eText, "contactInfo",
                            "Контактные данные"));

                    carInfo.add(new Element(Element.ElementType.eText, "certificateOfRegistration",
                            "Свидетельство о регистрации"));

                    carInfo.add(new Element(Element.ElementType.eText, "autoBrand", "Марка Модель"));

                    form.descriptionFields.add("autoBrand");

                    carInfo.add(new Element(Element.ElementType.eText, "plateNumber",
                            "Регистрационный номер"));

                    carInfo.add(new Element(Element.ElementType.eText, "yearOfManufacture",
                            "Год выпуска"));

                    carInfo.add(new Element(Element.ElementType.eText, "engineCapacity",
                            "Объем двигателя"));

                    carInfo.add(new Element(Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя",
                            new String[]{"Бензин", "Дизель", "Гибрид", "Газ"}));


                    carInfo.add(new Element(Element.ElementType.eText, "bodyNumber",
                            "Номер кузова"));

                    carInfo.add(new Element(Element.ElementType.eCombo, "bodyType", "Тип кузова",
                            new String[]{"Седан", "Купе", "Универсал", "Хэтчбэк", "Джип", "Пикап", "Минивэн", "Автобус",
                                    "Иное"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "numberOfDoors", "Количество дверей",
                            new String[]{"2-дверная", "3-дверная", "4-дверная", "5-дверная"}));

                    carInfo.add(new Element(Element.ElementType.eText, "bodyColor",
                            "Цвет кузова"));

                    carInfo.add(new Element(Element.ElementType.eCombo, "gearboxType", "Тип КПП",
                            new String[]{"автомат", "типтроник", "вариатор", "механика"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "driveType", "Тип привода",
                            new String[]{"передний", "задний", "полный"}));


                    carInfo.add(new Element(Element.ElementType.eCombo, "steering", "Расположение руля",
                            new String[]{"правый", "левый"}));


                    carInfo.add(new Element(Element.ElementType.eText, "Mileage",
                            "Пробег (километры, мили)"));

                    carInfo.add(new Element(Element.ElementType.eCombo, "interior", "Отделка салона",
                            new String[]{"кожаный", "велюровый", "синтетический"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "headlights", "Фары",
                            new String[]{"ксенон", "би-ксенон", "галоген"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "fogLights", "Противотуманные фары",
                            new String[]{"имеются", "отсутствуют"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "wheels", "Диски колес",
                            new String[]{"металлические", "легкосплавные"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "alarm", "Сигнализация",
                            new String[]{"заводская", "отсутствует", "самостоятельно установленная"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "storage", "Хранение",
                            new String[]{"охраняемая стоянка", "частная территория", "гараж", "подземный паркинг", "двор"}));

                    additionalInfo1.add(new Element(Element.ElementType.eText, "additionalInstallations",
                            "Кроме вышеперечисленного на ТС дополнительно установлено следующее:"));

                    additionalInfo1.add(new Element(Element.ElementType.eCombo, "conditionOfVehicle", "Состояние ТС",
                            new String[]{"новое", "бывшее в употреблении"}));


                    additionalInfo1.add(new Element(Element.ElementType.eText, "existingDamage",
                            "Имеющиеся повреждения"));


                    additionalInfo1.add(new Element(Element.ElementType.eCombo, "basisOfAssessment", "Основание",
                            new String[]{"рыночная", "балансовая", "договор купли-продажи", "счет-справка"}));


                    additionalInfo1.add(new Element(Element.ElementType.eText, "actualValue",
                            "Действительная стоимость"));

                    //signatures.add(new Element(Element.ElementType.eText, "fullName", "ФИО"));
                    form.elements.add(new Element(Element.ElementType.eSignature, "signature", "Подпись Клиента"));

                    break;
                case ("incident"):
                    ArrayList<Element> generalInfo = new ArrayList<>();
                    ArrayList<Element> insured = new ArrayList<>();
                    ArrayList<Element> otherCars = new ArrayList<>();
                    ArrayList<Element> secondParticipant = new ArrayList<>();
                    ArrayList<Element> thirdParticipant = new ArrayList<>();
                    ArrayList<Element> additionalInfo = new ArrayList<>();
                    ArrayList<Element> confirmation = new ArrayList<>();

                    form.elements.add(new Element(generalInfo, "generalInfo", "Общие данные"));
                    form.elements.add(new Element(insured, "insured", "Страхователь"));
                    form.elements.add(new Element(otherCars, "otherCars", "Другие участники ДТП"));
                    otherCars.add(new Element(secondParticipant, "secondParticipant", "Участник 2"));
                    otherCars.add(new Element(thirdParticipant, "thirdParticipant", "Участник 3"));
                    form.elements.add(new Element(additionalInfo, "additionalInfo", "Описание повреждений:"));

                    generalInfo.add(new Element(Element.ElementType.eText, "classOfInsurance",
                            "Класс страхования"));

                    generalInfo.add(new Element(Element.ElementType.eDateTime, "timeOfAccident",
                            "Дата и Время ДТП"));

                    generalInfo.add(new Element(Element.ElementType.eDateTime, "dateOfClaim",
                            "Время поступления вызова"));

                    generalInfo.add(new Element(Element.ElementType.eText, "nameOfProgram",
                            "Название программы"));

                    generalInfo.add(new Element(Element.ElementType.eText, "fullNameOfInspector",
                            "ФИО сотрудника, присутствовавшего на ДТП"));


                    insured.add(new Element(Element.ElementType.eText, "fullName", "ФИО"));

                    form.descriptionFields.add("fullName");

                    insured.add(new Element(Element.ElementType.eText, "owner", "собственник"));

                    insured.add(new Element(Element.ElementType.eText, "autoBrand", "Марка авто"));

                    insured.add(new Element(Element.ElementType.eText, "plateNumber",
                            "Регистрационный номер"));

                    insured.add(new Element(Element.ElementType.eText, "locationOfAccident",
                            "Место ДТП"));

                    insured.add(new Element(Element.ElementType.eText, "phone",
                            "Телефон"));

                    insured.add(new Element(Element.ElementType.eText, "status",
                            "Статус"));

                    insured.add(new Element(Element.ElementType.eText, "insuranceCompany",
                            "Название страховой компании"));

                    insured.add(new Element(Element.ElementType.eText, "policyNumber",
                            "Номер полиса"));

                    insured.add(new Element(Element.ElementType.eDate, "dateOfPolicyIssue",
                            "Дата выдачи полиса"));

                    insured.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom",
                            "от"));
                    insured.add(new Element(Element.ElementType.eDate, "periodOfValidityTo",
                            "до"));


                    secondParticipant.add(new Element(Element.ElementType.eText, "fullName2", "ФИО"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "owner2", "собственник"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "autoBrand2", "Марка авто"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "plateNumber2",
                            "Регистрационный номер"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "locationOfAccident2",
                            "Место ДТП"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "phoneNumber2",
                            "Телефон"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "status2",
                            "Статус"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "insuranceCompany2",
                            "Название страховой компании"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "policyNumber2",
                            "Номер полиса"));

                    secondParticipant.add(new Element(Element.ElementType.eDate, "dateOfPolicyIssue2",
                            "Дата выдачи полиса"));

                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom2",
                            "от"));
                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityTo2",
                            "до"));


                    thirdParticipant.add(new Element(Element.ElementType.eText, "fullName3", "ФИО"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "owner3", "собственник"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "autoBrand3", "Марка авто"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "plateNumber3",
                            "Регистрационный номер"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "locationOfAccident3",
                            "Место ДТП"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "phoneNumber3",
                            "Телефон"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "status3",
                            "Статус"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "insuranceCompany3",
                            "Название страховой компании"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "policyNumber3",
                            "Номер полиса"));

                    thirdParticipant.add(new Element(Element.ElementType.eDate, "dateOfPolicyIssue3",
                            "Дата выдачи полиса"));

                    thirdParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom3",
                            "от"));
                    thirdParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityTo3",
                            "до"));


                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamages",
                            "А/м страхователя"));
                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamagesSecond",
                            "2-ой участник"));
                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamagesThird",
                            "3-ий участник"));


                    additionalInfo.add(new Element(Element.ElementType.ePlan, "damagePlan", "План повреждений"));

                    additionalInfo.add(new Element(Element.ElementType.eText, "medicalAssistance",
                            "Наличие пострадавших, обратившихся за мед. помощью"));


                    additionalInfo.add(new Element(Element.ElementType.eCombo, "issuedDirection", "Выдано направление в НЭОЦ",
                            new String[]{"Да", "Нет"}));
                    additionalInfo.add(new Element(Element.ElementType.eCombo, "bookedWithPolice", "Оформлено с Дорожной Полицией",
                            new String[]{"Да", "Нет"}));
                    additionalInfo.add(new Element(Element.ElementType.eCombo, "serviceStation", "Выдано направление на Спец. СТО",
                            new String[]{"Да", "Нет"}));

                    additionalInfo.add(new Element(Element.ElementType.eText, "nameOfServiceStation",
                            "название"));

                    additionalInfo.add(new Element(Element.ElementType.eAnima, "schemeOfAccident", "Схема ДТП"));
                    additionalInfo.add(new Element(Element.ElementType.ePlan, "damagePlan", "План повреждений"));
                    additionalInfo.add(new Element(Element.ElementType.ePhoto, "generalPhotos", "Фотографии"));

                    form.elements.add(new Element(Element.ElementType.eSignature, "signature", "Подпись Клиента"));
                    break;
                default:
                    break;
            }
            //now insert values from "input" field, posted by INSIS
            form.applyInput(form.elements);
        }

    }

}
