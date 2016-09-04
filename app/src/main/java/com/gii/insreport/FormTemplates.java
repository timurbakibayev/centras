package com.gii.insreport;

import java.util.Date;

/**
 * Created by Timur_hnimdvi on 07-Aug-16.
 */
public class FormTemplates {
    public static String selectionTypes = "";

    public static void applyTemplateForParticipants(Element element) {
        element.elements.add(new Element("participant", Element.ElementType.eText, "PERSON_NAME",
                "ФИО персоны"));

        element.elements.add(new Element("participant", Element.ElementType.eText, "PERSON_TYPE",
                "Тип персоны", "DCT_PERSON_TYPE"));

        element.elements.add(new Element("participant", Element.ElementType.eText, "PERSON_IIN",
                "Иин персоны"));

        element.elements.add(new Element("participant", Element.ElementType.eText, "GUILT_PERCENTAGE",
                "% виновности"));

        element.elements.add(new Element("participant", Element.ElementType.eText, "GUILTY_CONTRACT_NO",
                "Номер договора другой страховой компании"));

        element.elements.add(new Element("participant", Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
                "Дата договора другой страховой компании"));

        element.elements.add(new Element("participant", Element.ElementType.eCombo, "THIRD_PART_INSURER",
                "страховая компания", "DCT_THIRD_PART_INSURER"));
    }

    public static void applyTemplateForObjects(Element form) {

        form.elements.add(new Element("object", Element.ElementType.ePlan, "DAMAGE_PLAN", "Повреждения"));

        form.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_TYPE",
                "тип объекта",
                "DCT_OBJECT_TYPE"));

        form.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_SUB_TYPE",
                "подтип объекта",
                "DCT_OBJECT_SUB_TYPE"));

        form.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_PRODUCTION",
                "марка",
                "DCT_OBJECT_PRODUCTION"));

        form.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_MODEL",
                "модель",
                "DCT_OBJECT_MODEL"));

        form.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_CHASSIS_NO_VIN",
                "№ кузова, № шасси"));

        form.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_REGISRATION_NUMBER",
                "Регистрационный номер объекта"));

        form.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_ENGINE_NO",
                "Номер двигателя"));

        form.elements.add(new Element("object", Element.ElementType.eText, "PRODUCTION_YEAR",
                "Год производства"));

        form.elements.add(new Element("object", Element.ElementType.eText, "PRODUCTION_MONTH",
                "Месяц производства"));

        form.elements.add(new Element("object", Element.ElementType.eText, "CAR_COLOUR",
                "Цвет объекта"));

    }

    public static void applyTemplate(Form form, String fireBaseCatalog) {
        form.dateModified = new Date(); //ServerValue.TIMESTAMP;
        form.fireBaseCatalog = fireBaseCatalog;
        if (form.elements.size() == 0) { //this is the first load of the form after server initiation. Or manually created form.
            switch (fireBaseCatalog) {
                case ("preInsurance"):
                    if (selectionTypes.equalsIgnoreCase("Car")) {
                        //ArrayList<Element> header = new ArrayList<>();
                        //ArrayList<Element> personalInfo = new ArrayList<>();
                        //ArrayList<Element> carInfo = new ArrayList<>();
                        //ArrayList<Element> additionalInfo1 = new ArrayList<>();

                        /*
                        form.elements.add(new Element(header, "header", "Заголовок"));

                        form.elements.add(new Element(personalInfo, "personalInfo", "Личные данные"));

                        form.elements.add(new Element(carInfo, "carInfo", "Данные по машине"));

                        form.elements.add(new Element(additionalInfo1, "additionalInfo1", "Дополнительные данные"));

                        */

                        form.elements.add(new Element("general", Element.ElementType.eText, "city", "Город"));

                        form.elements.add(new Element("general", Element.ElementType.eDate, "reportDate",
                                "Дата составления акта"));

                        form.elements.add(new Element("general", Element.ElementType.eText, "employee",
                                "представитель АО «СК «Сентрас Иншуранс» - ФИО"));

                        form.elements.add(new Element("general", Element.ElementType.eText, "client",
                                "клиента - ФИО"));

                        form.elements.add(new Element("general", Element.ElementType.eText, "address",
                                "по адресу"));

                        form.elements.add(new Element("personal", Element.ElementType.eText, "owner", "Владелец"));

                        form.descriptionFields.add("owner");

                        form.elements.add(new Element("personal", Element.ElementType.eText, "contactInfo",
                                "Контактные данные"));
                        /*

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
                        */
                    } else {
                        /*
                        ArrayList<Element> generalInfo1 = new ArrayList<>();
                        ArrayList<Element> carInfo = new ArrayList<>();
                        ArrayList<Element> regularSet = new ArrayList<>();
                        ArrayList<Element> damageInfo = new ArrayList<>();
                        ArrayList<Element> additionEquipment = new ArrayList<>();
                        ArrayList<Element> alarmSystem = new ArrayList<>();
                        */

                        form.elements.add(new Element("general", Element.ElementType.eText, "numberOfPolice",
                                "№"));
                        form.elements.add(new Element("general", Element.ElementType.eDate, "date",
                                "от"));

                        form.elements.add(new Element("car", Element.ElementType.eText, "model", "Марка/модель ТС"));
                        form.elements.add(new Element("car", Element.ElementType.eText, "productionDate", "Год выпуска"));
                        form.elements.add(new Element("car", Element.ElementType.eText, "VIN", "VIN (№ кузова/шасси)"));
                        form.elements.add(new Element("car", Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя ",
                                new String[]{"бензиновый", "электродвигатель"}));

                        form.elements.add(new Element("car", Element.ElementType.eText, "engineVolume", "Объем двигателя"));
                        form.elements.add(new Element("car", Element.ElementType.eRadio, "driveType", "Тип привода",
                                new String[]{"передний", "задний"}));
                        form.elements.add(new Element("car", Element.ElementType.eCombo, "gearboxType", "Тип КПП",
                                new String[]{"автомат", "типтроник", "вариатор", "механика"}));
                        form.elements.add(new Element("car", Element.ElementType.eText, "plateNumber", "Гос.рег.номер"));

                        form.elements.add(new Element("car", Element.ElementType.eText, "mileage", "Пробег (на дату осмотра)миль/км"));
                        form.elements.add(new Element("car", Element.ElementType.eText, "owner", "Владелец  ТС:"));
                        form.elements.add(new Element("car", Element.ElementType.eText, "registration",
                                "Свидетельство о регистрации ТС:"));
                        form.elements.add(new Element("car", Element.ElementType.eText, "series", "Серия №"));

                        form.elements.add(new Element("car", Element.ElementType.eText, "issued", "Выдан:"));

                        form.elements
                                .add(new Element("extra", Element.ElementType.eText, "name", "Название"));
                        form.elements
                                .add(new Element("extra", Element.ElementType.eText, "price", "Стоимость"));

                        if (selectionTypes.equalsIgnoreCase("Bike")) {
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "microcar",
                                    "Мотоколяска "));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "motokofr",
                                    "Мотокофр"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "motobagazhnik",
                                    "Мотобагажник "));
                            form.elements.add(new Element("car", Element.ElementType.eRadio, "headlights",
                                    "Фары",
                                    new String[]{"противотуманные", "ксеноновые"}));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "radio",
                                    "Радио/музыкальное оборудование (автомагнитола)"));
                            form.elements.add(new Element("car", Element.ElementType.eText, "rims",
                                    "Покрышки (фирма,модель)"));
                            form.elements.add(new Element("car", Element.ElementType.eText, "other",
                                    "Другое штатное заводское оборудование:"));
                            VehicleDamageView.carType = "Bike";
                        } else if (selectionTypes.equalsIgnoreCase("Bus") ||
                                selectionTypes.equalsIgnoreCase("Truck")) {
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "hatch", "люк"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "fogHeadlights",
                                    "противотуманные фарые"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "xenon",
                                    "ксеноновые фары"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "airConditioning",
                                    "кондиционер"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "climateControl;",
                                    "климат-контроль"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "centralLocking;",
                                    "центральный замок"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "navigationSystem;",
                                    "навигационная система"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "headlightWasher",
                                    "омыватель фар"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "wiper",
                                    "обогреватель щеток стеклоочистителя"));
                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));

                            form.elements.add(new Element("car", Element.ElementType.eCombo, "sensors",
                                    "Датчик парковки",
                                    new String[]{"задний", "передний", "дождя", "света"}));
                            form.elements.add(new Element("car", Element.ElementType.eRadio, "interior",
                                    "Салон", new String[]{"ткань", "синтетика", "кожа"}));
                            form.elements.add(new Element("car", Element.ElementType.eText, "bodyPaint", "Цвет кузова"));
                            form.elements.add(new Element("car", Element.ElementType.eRadio, "tuning",
                                    "Тюнинг", new String[]{"есть", "нет"}));
                            form.elements.add(new Element("car", Element.ElementType.eText, "tuning",
                                    "наименование"));
                            form.elements.add(new Element("car", Element.ElementType.eRadio, "electricDrive",
                                    "Электропривод", new String[]{"зеркала", "стеклоподъемники",
                                    "сиденья"}));
                            form.elements.add(new Element("car", Element.ElementType.eRadio, "electricHeater",
                                    "Электроподогрев", new String[]{"зеркала", "сиденья"}));
                            form.elements.add(new Element("car", Element.ElementType.eCombo, "equipment",
                                    "Электроподогрев", new String[]{"заводская магнитола",
                                    "заводской CD-чейнджер", "Встр. динамики"}));
                            form.elements.add(new Element("car", Element.ElementType.eCombo, "rims",
                                    "Колесные диски", new String[]{"Литые", "Стальные",
                                    "Колпаки", "Резина"}));
                            form.elements.add(new Element("car", Element.ElementType.eText, "otherEquipment",
                                    "Другое оборудование"));
                            if (selectionTypes.equalsIgnoreCase("Bus")) {
                                VehicleDamageView.carType = "Bus";


                            } else if (selectionTypes.equalsIgnoreCase(("Truck"))) {
                                VehicleDamageView.carType = "Truck";
                            }
                        }

                        /*
                        damageInfo.add(new Element(Element.ElementType.ePlan, "damagePlan",
                                "План повреждений"));
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
                        form.elements.add(new Element(Element.ElementType.eSignature, "signatureEmployee", "Представитель Страховщика"));
                        */
                    }
//
                    break;

                case ("incident"):

                    form.elements.add(new Element("general", Element.ElementType.eText, "DOCUMENT_ID",
                            "Системный код"));

                    form.elements.add(new Element("general", Element.ElementType.eLookUp, "DOCUMENT_TYPE",
                            "Тип документа",
                            "DCT_DOCUMENT_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLAIM_REGID",
                            "Номер дела"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "CLAIM_TYPE",
                            "Тип дела",
                            "DCT_CLAIM_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eDateTime, "CLAIM_STARTED",
                            "Дата регистрации дела"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "CAUSE_ID",
                            "Тип урегулирования",
                            "DCT_CAUSE_ID"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "POLICY_REG",
                            "№ Договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_DATE_GIVEN",
                            "Дата выдачи договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_INSR_BEGIN",
                            "начало действия"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_INSR_END",
                            "конец действия"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_REG",
                            "№ Договора"));
                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_DATE_GIVEN",
                            "Дата выдачи договора"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_INSR_BEGIN",
                            "Начало действия"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_INSR_END",
                            "Конец действия"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "INSR_TYPE",
                            "Код продукта страхования",
                            "DCT_INSR_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО Страхователя"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО Страхователя"));

                    form.elements.add(new Element("general", Element.ElementType.eDateTime, "EVENT_DATE",
                            "Дата страх. случая"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "DCT_EVENT_TYPE",
                            "Причина/Событие страх. Случая", "DCT_EVENT_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "EVENT_PLACE",
                            "Место события"));
                    form.elements.add(new Element("general", Element.ElementType.eText, "EVENT_DESCRIPTION",
                            "Описание события"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "INITIAL_SUM",
                            "Предварительная сумма (ущерба для страховых случае, стоимости авто для предварительного страхового осмотра)"));

                    form.elements.add(new Element("", Element.ElementType.eDate, "REGISTRATION_DATE",
                            "Системное/Серверное время создания записи"));
                    form.elements.add(new Element("", Element.ElementType.eText, "USERNAME",
                            "Пользователь создавший запись (Login)"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGED_BY",
                            "Пользователь изменивший запись (Login)"));
                    form.elements.add(new Element("", Element.ElementType.eDate, "CHANGE_DATE",
                            "Системное/Серверное время изменения записи"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "OPERATOR_NAME",
                            "Имя оператора кол-центра"));
                    form.elements.add(new Element("", Element.ElementType.eText, "SEND_SMS",
                            "На какой номер АК было отправлено уведомление о страх. Случае"));
                    form.elements.add(new Element("general", Element.ElementType.eText, "CLAIMANT_PHONE_NO",
                            "Номер телефона застрахованного"));
                    form.elements.add(new Element("general", Element.ElementType.eText, "SMS_PROVIDER",
                            "СМС провайдер"));
                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "SPECIALIST_SIGN",
                            "Подпись Комисcара"));
                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "CLIENT_SIGN",
                            "Подпись Клиента"));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto,"photoDocuments","Документы"));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto,"photoDamages","Повреждения"));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto,"photoOther","Другое"));


                    //form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "DOCUMENT_ID",
                    //        "Ссылка на основную запись в таблице общие данные"));

                    //form.elements.add(new Element("additionalInfo", Element.ElementType.eCombo, "QUESTION_ID",
                    //        "Системный код/счетчик дела (sequence)" +
                    //                "FireBase"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_CODE",
                            "Код вопроса"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_DESC",
                            "Описание вопроса"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_ANSWER_TYPE",
                            "Тип ответа"));
                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_ANSWER",
                            "Ответ на вопрос", "DCT_QUESTION_ANSWER"));


                    /*
                    form.elements.add(new Element("participant", Element.ElementType.eText, "DOCUMENT_ID",
                            "Ссылка на основную запись в таблице общие данные"));
                    form.elements.add(new Element("participant", Element.ElementType.eText, "PERSON_ID",
                            "Системный код/счетчик дела (sequence)" +
                                    "FireBase"));
                                    */



                    //form.elements.add(new Element("object", Element.ElementType.eText, "DOCUMENT_ID",
                    //        "Ссылка на основную запись в таблице общие данные"));

                    //form.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_ID",
                    //        "Системный код/счетчик дела (sequence)" +
                    //                "FireBase"));



                    //////////////////////////////////////




                    /*
                    form.elements.add(new Element("attachments", Element.ElementType.eText, "DOCUMENT_ID",
                            "Ссылка на основную запись в таблице общие данные"));

                    form.elements.add(new Element("attachments", Element.ElementType.eText, "OBJECT_ID",
                            "Системный код/счетчик дела (sequence)" +
                                    "FireBase"));

                    form.elements.add(new Element("attachments", Element.ElementType.eText, "ATTACHMENT_TYPE",
                            "Тип документа",
                            "DCT_ATTACHMENT_TYPE"));

                    form.elements.add(new Element("attachments", Element.ElementType.eText, "ATTACHMENT_COMMENTS",
                            "Комментарии по документу"));

                    */
                    /*
                    attachments.add(new Element(Element.ElementType.eCombo, "DCT_ATTACHMENT_TYPE",
                            "Тип документа",
                            new String[]{"Список движимого/недвижимого имущества (ЖД ТС, скважин, Упаковочный лист)",
                                    "График Строительства, Полная смета затрат",
                                    "Лицензия на осуществление деятельности",
                                    "Вид на жительство",
                                    "Удостоверение лица без гражданства",
                                    "Миграционная карточка",
                                    "Виза",
                                    "Заявление-анкета на страхование",
                                    "Фотографии осмотра груза при погрузке",
                                    "Фотографии осмотра груза при разгрузке",
                                    "Документы, подтверждающие стоимость недвижимости, оборудования, товара",
                                    "Уведомление ФМ-1 в Комитет – пороговые",
                                    "Технический паспорт здания (сооружения)",
                                    "Выписки протокола АС/андеррайтинговое решение",
                                    "Уведомление ФМ-1 в Комитет – подозрительные",
                                    "Нотариально заверенная доверенность на совершение юридически значимых действий от имени физ.лица",
                                    "Адресная справка/иной документ подтверждающий место жительства",
                                    "Документ о получении образования (диплом, аттестат)",
                                    "Заявление-анкета агента",
                                    "Свидетельство о регистрации транспортного средства",
                                    "Свидетельство о расторжении брака",
                                    "Свидетельство о браке",
                                    "Свидетельство о рождении",
                                    "Свидетельство агента о прохождении минимальной программы обучения",
                                    "Документ, на основании которого лицо осуществляет функции единоличного исполнительного органа/руководителя/члена коллегиального исполнительного органа",
                                    "Документ, на основании которого установлен состав высшего органа юридического лица",
                                    "Документ, на основании которого установлена структура органов юр.лица",
                                    "Свидетельство об учетной регистрации филиала (представительства) юр.лица",
                                    "Заявление агента на прохождение минимальной программы обучения",
                                    "Договор поручения с агентом",
                                    "Документ (приказ, доверенность), предоставляющий представителю право совершать юридически значимые действия от имени юр. лица",
                                    "Опись имущества, подлежащего страхованию",
                                    "РНН - регистрационный номер налогоплательщика",
                                    "Декларации о промышленной безопасности",
                                    "Список Застрахованных (при страховании более 1 человека) ДМС/НС",
                                    "Водительское удостоверение",
                                    "Пенсионное удостоверение",
                                    "Удостоверение инвалида I и II группы",
                                    "Удостоверение личности",
                                    "Отчет об оценке залогового имущества",
                                    "Договор страхования",
                                    "Свидетельство о государственной регистрации юридического лица/ИП",
                                    "ИИН - индивидуальный идентификационный номер",
                                    "Фотографии застрахованного объекта",
                                    "Договор залога",
                                    "Другие документы",
                                    "Фотографии с места ДТП",
                                    "Документы на предоставление льготы",
                                    "Заявление на сезонную эксплуатацию автомобиля",
                                    "БИН - бизнес-идентификационный номер",
                                    "Уведомление о включении в Реестр",
                                    "Список застрахованных объектов",
                                    "Акт осмотра",
                                    "Документ о регистрации от уполномоченного органа",
                                    "Устав",
                                    "Справка об отсутствии судимости",
                                    "Справка о дееспособности",
                                    "Удостоверение участника Великой отечественной войны или лица, приравненные к участнику ВОВ",
                                    "Доверенность на право управления ТС",
                                    "Статистическая карта",
                                    "Правоустанавливающие документы на объект",
                                    "Паспорт"}));


                    attachments.add(new Element(Element.ElementType.eText, "ATTACHMENT_COMMENTS",
                            "Комментарии по документу"));

                    attachments.add(new Element(Element.ElementType.eText, "USERNAME",
                            "Пользователь создавший запись (Login)"));
                    attachments.add(new Element(Element.ElementType.eText, "REGISTRATION_DATE",
                            "Системное/Серверное время создания записи"));
                    attachments.add(new Element(Element.ElementType.eText, "CHANGED_BY",
                            "Пользователь изменивший запись (Login)"));
                    attachments.add(new Element(Element.ElementType.eText, "CHANGE_DATE",
                            "Системное/Серверное время изменения записи"));

                    */

//                    ArrayList<Element> generalInfo = new ArrayList<>();
//                    ArrayList<Element> insured = new ArrayList<>();
//                    ArrayList<Element> otherCars = new ArrayList<>();
//                    ArrayList<Element> secondParticipant = new ArrayList<>();
//                    ArrayList<Element> thirdParticipant = new ArrayList<>();
//                    ArrayList<Element> additionalInfo = new ArrayList<>();
//                    ArrayList<Element> confirmation = new ArrayList<>();
//
//                    form.elements.add(new Element(generalInfo, "generalInfo", "Общие данные"));
//                    form.elements.add(new Element(insured, "insured", "Страхователь"));
//                    form.elements.add(new Element(otherCars, "otherCars", "Другие участники ДТП"));
//                    otherCars.add(new Element(secondParticipant, "PARTICIPANTS_INFO", "Участник 2"));
//                    otherCars.add(new Element(thirdParticipant, "PARTICIPANTS_INFO", "Участник 3"));
//                    form.elements.add(new Element(additionalInfo, "CLAIM_COMMENT", "Описание повреждений:"));
//
//                    generalInfo.add(new Element(Element.ElementType.eText, "INSR_CLASS",
//                            "Класс страхования"));
//
//                    //TODO: WRONG!!! SAMPLE!
//                    generalInfo.add(new Element(Element.ElementType.eCombo, "DOCUMENT_TYPE", "Тип документа", "DCT_CLAIM_TYPE"));
//
//                    generalInfo.add(new Element(Element.ElementType.eDateTime, "EVENT_DATE",
//                            "Дата и Время ДТП"));
//
//                    generalInfo.add(new Element(Element.ElementType.eDateTime, "CLAIM_STARTED",
//                            "Время поступления вызова"));
//
//                    generalInfo.add(new Element(Element.ElementType.eCombo, "DCT_CAUSE_ID", "Тип урегулирования",
//                            new String[]{"Стандартное урегулирование",
//                            "Прямое урегулирование", "Ответственное урегулирование"}));
//
//                    additionalInfo.add(new Element(Element.ElementType.eCombo, "serviceStation", "Выдано направление на Спец. СТО",
//                            new String[]{"Да", "Нет"}));
//
//                    generalInfo.add(new Element(Element.ElementType.eText, "nameOfProgram",
//                            "Название программы"));
//
//                    generalInfo.add(new Element(Element.ElementType.eText, "SPECIALIST_NAME",
//                            "ФИО сотрудника, присутствовавшего на ДТП"));
//
//
//                    insured.add(new Element(Element.ElementType.eText, "CLIENT_NAME", "ФИО"));
//
//                    form.descriptionFields.add("fullName");
//
//                    insured.add(new Element(Element.ElementType.eText, "owner", "собственник"));
//
//                    insured.add(new Element(Element.ElementType.eText, "OBJECT_PRODUCTION", "Марка авто"));
//
//                    insured.add(new Element(Element.ElementType.eText, "OBJECT_CHASSIS_NO_VIN",
//                            "Регистрационный номер"));
//
//                    insured.add(new Element(Element.ElementType.eText, "EVENT_PLACE",
//                            "Место ДТП"));
//
//                    insured.add(new Element(Element.ElementType.eText, "CLAIMANT_PHONE_NO",
//                            "Телефон"));
//
//                    insured.add(new Element(Element.ElementType.eText, "status",
//                            "Статус"));
//
//                    insured.add(new Element(Element.ElementType.eText, "insuranceCompany",
//                            "Название страховой компании"));
//
//                    insured.add(new Element(Element.ElementType.eText, "POLICY_REG",
//                            "Номер полиса"));
//
//                    insured.add(new Element(Element.ElementType.eDate, "POLICY_DATE_GIVEN",
//                            "Дата выдачи полиса"));
//
//                    insured.add(new Element(Element.ElementType.eDate, "POLICY_INSR_BEGIN",
//                            "от"));
//                    insured.add(new Element(Element.ElementType.eDate, "POLICY_INSR_END",
//                            "до"));
//
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "PERSON_NAME", "ФИО"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "owner2", "собственник"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "autoBrand2", "Марка авто"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "plateNumber2",
//                            "Регистрационный номер"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "locationOfAccident2",
//                            "Место ДТП"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "phoneNumber2",
//                            "Телефон"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "status2",
//                            "Статус"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "THIRD_PART_INSURER",
//                            "Название страховой компании"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eText, "GUILTY_CONTRACT_NO",
//                            "Номер полиса"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
//                            "Дата выдачи полиса"));
//
//                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom2",
//                            "от"));
//                    secondParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityTo2",
//                            "до"));
//
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "PERSON_NAME", "ФИО"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "owner3", "собственник"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "autoBrand3", "Марка авто"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "plateNumber3",
//                            "Регистрационный номер"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "locationOfAccident3",
//                            "Место ДТП"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "phoneNumber3",
//                            "Телефон"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "status3",
//                            "Статус"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "THIRD_PART_INSURER",
//                            "Название страховой компании"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eText, "GUILTY_CONTRACT_NO",
//                            "Номер полиса"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
//                            "Дата выдачи полиса"));
//
//                    thirdParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityFrom3",
//                            "от"));
//                    thirdParticipant.add(new Element(Element.ElementType.eDate, "periodOfValidityTo3",
//                            "до"));
//
//
//                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamages",
//                            "А/м страхователя"));
//                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamagesSecond",
//                            "2-ой участник"));
//                    additionalInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamagesThird",
//                            "3-ий участник"));
//
//
//                    additionalInfo.add(new Element(Element.ElementType.ePlan, "damagePlan", "План повреждений"));
//
//                    additionalInfo.add(new Element(Element.ElementType.eText, "medicalAssistance",
//                            "Наличие пострадавших, обратившихся за мед. помощью"));
//
//
//                    additionalInfo.add(new Element(Element.ElementType.eCombo, "issuedDirection", "Выдано направление в НЭОЦ",
//                            new String[]{"Да", "Нет"}));
//                    additionalInfo.add(new Element(Element.ElementType.eCombo, "bookedWithPolice", "Оформлено с Дорожной Полицией",
//                            new String[]{"Да", "Нет"}));
//                    additionalInfo.add(new Element(Element.ElementType.eCombo, "serviceStation", "Выдано направление на Спец. СТО",
//                            new String[]{"Да", "Нет"}));
//
//                    additionalInfo.add(new Element(Element.ElementType.eText, "nameOfServiceStation",
//                            "название"));
//
//                    additionalInfo.add(new Element(Element.ElementType.eAnima, "schemeOfAccident", "Схема ДТП"));
//                    additionalInfo.add(new Element(Element.ElementType.ePlan, "damagePlan", "План повреждений"));
//                    additionalInfo.add(new Element(Element.ElementType.ePhoto, "generalPhotos", "Фотографии"));
//
//                    form.elements.add(new Element(Element.ElementType.eSignature, "CLIENT_SIGN", "Подпись Клиента"));
                    break;
                default:
                    break;
            }
            //now insert values from "input" field, posted by INSIS
            form.applyInput(form.elements);
        }

    }

}
