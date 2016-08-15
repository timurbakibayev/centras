package com.gii.insreport;

import android.widget.Toast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Timur_hnimdvi on 07-Aug-16.
 */
public class FormTemplates {
    public static String selectionTypes = "";

    public static void applyTemplate(Form form, String fireBaseCatalog) {
        form.dateModified = new Date(); //ServerValue.TIMESTAMP;
        form.fireBaseCatalog = fireBaseCatalog;
        if (form.elements.size() == 0) { //this is the first load of the form after server initiation. Or manually created form.
            switch (fireBaseCatalog) {
                case ("preInsurance"):
                    if (selectionTypes.equalsIgnoreCase("Car")) {
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

                    carInfo.add(new Element(Element.ElementType.eRadio, "driveType", "Тип привода",
                            new String[]{"передний", "задний", "полный"}));


                    carInfo.add(new Element(Element.ElementType.eRadio, "steering", "Расположение руля",
                            new String[]{"правый", "левый"}));


                    carInfo.add(new Element(Element.ElementType.eText, "Mileage",
                            "Пробег (километры, мили)"));

                    carInfo.add(new Element(Element.ElementType.eRadio, "interior", "Отделка салона",
                            new String[]{"кожаный", "велюровый", "синтетический"}));

                    carInfo.add(new Element(Element.ElementType.eRadio, "headlights", "Фары",
                            new String[]{"ксенон", "би-ксенон", "галоген"}));

                    carInfo.add(new Element(Element.ElementType.eRadio, "fogLights", "Противотуманные фары",
                            new String[]{"имеются", "отсутствуют"}));

                    carInfo.add(new Element(Element.ElementType.eRadio, "wheels", "Диски колес",
                            new String[]{"металлические", "легкосплавные"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "alarm", "Сигнализация",
                            new String[]{"заводская", "отсутствует", "самостоятельно установленная"}));

                    carInfo.add(new Element(Element.ElementType.eCombo, "storage", "Хранение",
                            new String[]{"охраняемая стоянка", "частная территория", "гараж", "подземный паркинг", "двор"}));

                    additionalInfo1.add(new Element(Element.ElementType.eText, "additionalInstallations",
                            "Кроме вышеперечисленного на ТС дополнительно установлено следующее:"));

                    additionalInfo1.add(new Element(Element.ElementType.eRadio, "conditionOfVehicle", "Состояние ТС",
                            new String[]{"новое", "бывшее в употреблении"}));


                    additionalInfo1.add(new Element(Element.ElementType.eText, "existingDamage",
                            "Имеющиеся повреждения"));


                    additionalInfo1.add(new Element(Element.ElementType.eCombo, "basisOfAssessment", "Основание",
                            new String[]{"рыночная", "балансовая", "договор купли-продажи", "счет-справка"}));


                    additionalInfo1.add(new Element(Element.ElementType.eText, "actualValue",
                            "Действительная стоимость"));

                    //signatures.add(new Element(Element.ElementType.eText, "fullName", "ФИО"));
                    form.elements.add(new Element(Element.ElementType.eSignature, "signature", "Подпись Клиента"));
                    } else {
                        ArrayList<Element> generalInfo1 = new ArrayList<>();
                        ArrayList<Element> carInfo = new ArrayList<>();
                        ArrayList<Element> regularSet = new ArrayList<>();
                        ArrayList<Element> damageInfo = new ArrayList<>();
                        ArrayList<Element> additionEquipment = new ArrayList<>();
                        ArrayList<Element> alarmSystem = new ArrayList<>();

                        form.elements.add(new Element(generalInfo1, "generalInfo", "Общие данные"));
                        form.elements.add(new Element(carInfo, "carInfo", "Информация о ТС"));
                        form.elements.add(new Element(regularSet, "regularSet",
                                "Описание штатной комплектации"));
                        form.elements.add(new Element(additionEquipment, "additionEquipment",
                                "Дополнительное оборудование, подлежащее страхованию"));
                        form.elements.add(new Element(damageInfo, "damageInfo",
                                "Отметки об имеющихся повреждениях"));
                        form.elements.add(new Element(alarmSystem, "alarmSystem",
                                "ТС оборудовано следующими исправными средствами противоугонной защиты (СИСТЕМАМИ)"));


                        generalInfo1.add(new Element(Element.ElementType.eText, "numberOfPolice",
                                "№"));
                        generalInfo1.add(new Element(Element.ElementType.eDate, "date",
                                "от"));

                        carInfo.add(new Element(Element.ElementType.eText, "model", "Марка/модель ТС"));
                        carInfo.add(new Element(Element.ElementType.eText, "productionDate", "Год выпуска"));
                        carInfo.add(new Element(Element.ElementType.eText, "VIN", "VIN (№ кузова/шасси)"));
                        carInfo.add(new Element(Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя ",
                                new String[]{"бензиновый", "электродвигатель"}));

                        carInfo.add(new Element(Element.ElementType.eText, "engineVolume", "Объем двигателя"));
                        carInfo.add(new Element(Element.ElementType.eRadio, "driveType", "Тип привода",
                                new String[]{"передний", "задний"}));
                        carInfo.add(new Element(Element.ElementType.eCombo, "gearboxType", "Тип КПП",
                                new String[]{"автомат", "типтроник", "вариатор", "механика"}));
                        carInfo.add(new Element(Element.ElementType.eText, "plateNumber", "Гос.рег.номер"));

                        carInfo.add(new Element(Element.ElementType.eText, "mileage", "Пробег (на дату осмотра)миль/км"));
                        carInfo.add(new Element(Element.ElementType.eText, "owner", "Владелец  ТС:"));
                        carInfo.add(new Element(Element.ElementType.eText, "registration",
                                "Свидетельство о регистрации ТС:"));
                        carInfo.add(new Element(Element.ElementType.eText, "series", "Серия №"));

                        carInfo.add(new Element(Element.ElementType.eText, "issued", "Выдан:"));

                        additionEquipment
                                .add(new Element(Element.ElementType.eText, "name", "Название"));
                        additionEquipment
                                .add(new Element(Element.ElementType.eText, "price", "Стоимость"));

                        if (selectionTypes.equalsIgnoreCase("Bike")) {
                            regularSet.add(new Element(Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "microcar",
                                    "Мотоколяска "));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "motokofr",
                                    "Мотокофр"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "motobagazhnik",
                                    "Мотобагажник "));
                            regularSet.add(new Element(Element.ElementType.eRadio, "headlights",
                                    "Фары",
                                    new String[]{"противотуманные", "ксеноновые"}));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "radio",
                                    "Радио/музыкальное оборудование (автомагнитола)"));
                            regularSet.add(new Element(Element.ElementType.eText, "rims",
                                    "Покрышки (фирма,модель)"));
                            regularSet.add(new Element(Element.ElementType.eText, "other",
                                    "Другое штатное заводское оборудование:"));
                            damageInfo.add(new Element(Element.ElementType.ePlan, "damagePlan",
                                    "План повреждений"));


                        } else if (selectionTypes.equalsIgnoreCase("Bus") ||
                                selectionTypes.equalsIgnoreCase("Truck")) {
                            regularSet.add(new Element(Element.ElementType.eBoolean, "hatch", "люк"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "fogHeadlights",
                                    "противотуманные фарые"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "xenon",
                                    "ксеноновые фары"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "airConditioning",
                                    "кондиционер"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "climateControl;",
                                    "климат-контроль"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "centralLocking;",
                                    "центральный замок"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "navigationSystem;",
                                    "навигационная система"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "headlightWasher",
                                    "омыватель фар"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "wiper",
                                    "обогреватель щеток стеклоочистителя"));
                            regularSet.add(new Element(Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));

                            regularSet.add(new Element(Element.ElementType.eCombo, "sensors",
                                    "Датчик парковки",
                                    new String[]{"задний", "передний", "дождя", "света"}));
                            regularSet.add(new Element(Element.ElementType.eRadio, "interior",
                                    "Салон", new String[]{"ткань", "синтетика", "кожа"}));
                            regularSet.add(new Element(Element.ElementType.eText, "bodyPaint", "Цвет кузова"));
                            regularSet.add(new Element(Element.ElementType.eRadio, "tuning",
                                    "Тюнинг", new String[]{"есть", "нет"}));
                            regularSet.add(new Element(Element.ElementType.eText, "tuning",
                                    "наименование"));
                            regularSet.add(new Element(Element.ElementType.eRadio, "electricDrive",
                                    "Электропривод", new String[]{"зеркала", "стеклоподъемники",
                                    "сиденья"}));
                            regularSet.add(new Element(Element.ElementType.eRadio, "electricHeater",
                                    "Электроподогрев", new String[]{"зеркала", "сиденья"}));
                            regularSet.add(new Element(Element.ElementType.eCombo, "equipment",
                                    "Электроподогрев", new String[]{"заводская магнитола",
                                    "заводской CD-чейнджер", "Встр. динамики"}));
                            regularSet.add(new Element(Element.ElementType.eCombo, "rims",
                                    "Колесные диски", new String[]{"Литые", "Стальные",
                                    "Колпаки", "Резина"}));
                            regularSet.add(new Element(Element.ElementType.eText, "otherEquipment",
                                    "Другое оборудование"));
                        } else if (selectionTypes.equalsIgnoreCase("Car")) {

                        } else if (selectionTypes.equalsIgnoreCase("Truck")) {

                        }
                        regularSet.add(new Element(Element.ElementType.eText, "keys", "Количество оригинальных  комплектов  ключей у Страхователя"));
                        regularSet.add(new Element(Element.ElementType.eText, "photos", "Количество фотографий ТС"));
                        regularSet.add(new Element(Element.ElementType.eText, "specialNotes:", "Особые отметки:"));
                        damageInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamages",
                                "Описание повреждений"));
                        alarmSystem.add(new Element(Element.ElementType.eText, "immobiliser",
                                "Иммобилайзер"));
                        alarmSystem.add(new Element(Element.ElementType.eText, "antiTheft",
                                "Электронная противоугонная система"));
                        alarmSystem.add(new Element(Element.ElementType.eText, "gps",
                                "Радиопоисковая система"));


                        form.elements.add(new Element(Element.ElementType.eText, "initiatorName",
                                "Ф.И.О. лица инициировавшего предстраховой осмотр ТС "));


                        form.elements.add(new Element(Element.ElementType.eDateTime, "from",
                                "с"));
                        form.elements.add(new Element(Element.ElementType.eDateTime, "finishTime",
                                "Время завершения осмотра"));

                        form.elements.add(new Element(Element.ElementType.eSignature, "signatureClient", "Представитель страхователя"));
                        form.elements.add(new Element(Element.ElementType.eSignature, "signatureEmploye", "Представитель Страховщика"));
                    }
//
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
                    otherCars.add(new Element(secondParticipant, "PARTICIPANTS_INFO", "Участник 2"));
                    otherCars.add(new Element(thirdParticipant, "PARTICIPANTS_INFO", "Участник 3"));
                    form.elements.add(new Element(additionalInfo, "CLAIM_COMMENT", "Описание повреждений:"));

                    generalInfo.add(new Element(Element.ElementType.eText, "INSR_CLASS",
                            "Класс страхования"));

                    generalInfo.add(new Element(Element.ElementType.eDateTime, "EVENT_DATE",
                            "Дата и Время ДТП"));

                    generalInfo.add(new Element(Element.ElementType.eDateTime, "CLAIM_STARTED",
                            "Время поступления вызова"));

                    generalInfo.add(new Element(Element.ElementType.eText, "nameOfProgram",
                            "Название программы"));

                    generalInfo.add(new Element(Element.ElementType.eText, "SPECIALIST_NAME",
                            "ФИО сотрудника, присутствовавшего на ДТП"));


                    insured.add(new Element(Element.ElementType.eText, "CLIENT_NAME", "ФИО"));

                    form.descriptionFields.add("fullName");

                    insured.add(new Element(Element.ElementType.eText, "owner", "собственник"));

                    insured.add(new Element(Element.ElementType.eText, "OBJECT_PRODUCTION", "Марка авто"));

                    insured.add(new Element(Element.ElementType.eText, "OBJECT_CHASSIS_NO_VIN",
                            "Регистрационный номер"));

                    insured.add(new Element(Element.ElementType.eText, "EVENT_PLACE",
                            "Место ДТП"));

                    insured.add(new Element(Element.ElementType.eText, "CLAIMANT_PHONE_NO",
                            "Телефон"));

                    insured.add(new Element(Element.ElementType.eText, "status",
                            "Статус"));

                    insured.add(new Element(Element.ElementType.eText, "insuranceCompany",
                            "Название страховой компании"));

                    insured.add(new Element(Element.ElementType.eText, "POLICY_REG",
                            "Номер полиса"));

                    insured.add(new Element(Element.ElementType.eDate, "POLICY_DATE_GIVEN",
                            "Дата выдачи полиса"));

                    insured.add(new Element(Element.ElementType.eDate, "POLICY_INSR_BEGIN",
                            "от"));
                    insured.add(new Element(Element.ElementType.eDate, "POLICY_INSR_END",
                            "до"));


                    secondParticipant.add(new Element(Element.ElementType.eText, "PERSON_NAME", "ФИО"));

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

                    secondParticipant.add(new Element(Element.ElementType.eText, "THIRD_PART_INSURER",
                            "Название страховой компании"));

                    secondParticipant.add(new Element(Element.ElementType.eText, "GUILTY_CONTRACT_NO",
                            "Номер полиса"));

                    secondParticipant.add(new Element(Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
                            "Дата выдачи полиса"));

                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom2",
                            "от"));
                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityTo2",
                            "до"));


                    thirdParticipant.add(new Element(Element.ElementType.eText, "PERSON_NAME", "ФИО"));

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

                    thirdParticipant.add(new Element(Element.ElementType.eText, "THIRD_PART_INSURER",
                            "Название страховой компании"));

                    thirdParticipant.add(new Element(Element.ElementType.eText, "GUILTY_CONTRACT_NO",
                            "Номер полиса"));

                    thirdParticipant.add(new Element(Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
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

                    form.elements.add(new Element(Element.ElementType.eSignature, "CLIENT_SIGN", "Подпись Клиента"));
                    break;
                default:
                    break;
            }
            //now insert values from "input" field, posted by INSIS
            form.applyInput(form.elements);
        }

    }

}
