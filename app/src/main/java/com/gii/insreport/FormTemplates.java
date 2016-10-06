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

        element.elements.add(new Element("participant", Element.ElementType.eComboMulti, "PERSON_TYPE",
                "Тип персоны", "DCT_PERSON_TYPE"));

        element.elements.add(new Element("participant", Element.ElementType.eTextNum, "PERSON_IIN",
                "Иин персоны"));

        element.elements.add(new Element("participant", Element.ElementType.eTextNum, "GUILT_PERCENTAGE",
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


        form.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_TYPE",
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

        form.elements.add(new Element("object", Element.ElementType.eTextNum, "PRODUCTION_YEAR",
                "Год производства"));

        form.elements.add(new Element("object", Element.ElementType.eTextNum, "PRODUCTION_MONTH",
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
                    form.elements.add(new Element("", Element.ElementType.eTextNum, "DOC_ID",
                            "Системный код заявления"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "DOC_NO",
                            "№ заявки"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "DOCUMENT_TYPE",
                            "Тип заявки"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "DOCUMENT DATE",
                            "Дата/время заявки"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "OFFICE_NAME",
                            "Наименование подразделения заявки"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "ISSUER_NAME",
                            "Менеджер заявки"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "INSR_TYPE_DESC",
                            "Наименование продукта"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО/Наименование страхователя"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLIENT_CONTACTS",
                            "Контакты страхователя"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_NO",
                            "№ Договора страхования"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "DATE_GIVEN",
                            "Дата/время выдачи договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "INSR_BEGIN",
                            "Дата/время начала договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "INSR_END",
                            "Дата/время окончания договора"));


                    form.elements.add(new Element("general", Element.ElementType.eTextNum, "INSURANCE_VALUE",
                            "Страховая сумма"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "ADDITIONAL_CONDITIONS",
                            "Дополнительные условия по договору"));

                    form.elements.add(new Element("", Element.ElementType.eText, "MANAGER_COMENTS",
                            "Комментарии менеджера"));

                    form.elements.add(new Element("", Element.ElementType.eDate, "CREATED_DATE",
                            "Системная дата создания записи"));

                    form.elements.add(new Element("", Element.ElementType.eDate, "CREATED_USER",
                            "Пользователь создавший запись"));

                    form.elements.add(new Element("", Element.ElementType.eDate, "CHANGED_DATE",
                            "Системная дата изменени записи"));

                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGED_USER",
                            "Пользователь Изменивший запись"));


                    form.elements.add(new Element("", Element.ElementType.eTextNum, "DOC_ID",
                            "Системный код заявления"));
                    form.elements.add(new Element("", Element.ElementType.eTextNum, "OBJECT_ID",
                            "код объекта"));
                    form.elements.add(new Element("", Element.ElementType.eTextNum, "DOCUMENT_ID",
                            "код документа"));

                    form.elements.add(new Element("documents", Element.ElementType.eText, "DOCUMENT_TYPE",
                            "Тип документа"));
                    form.elements.add(new Element("documents", Element.ElementType.eText, "DOCUMENT_NO",
                            "№ документа"));

                    form.elements.add(new Element("documents", Element.ElementType.eDate, "DOCUMENT_DATE",
                            "дата выдачи документа"));

                    form.elements.add(new Element("documents", Element.ElementType.ePhoto, "DOCUMENT_ATTACHMENT",
                            "прикрепление изображения"));

                    form.elements.add(new Element("", Element.ElementType.eDate, "CREATED_DATE",
                            "Системная дата создания записи"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CREATED_USER",
                            "Пользователь создавший запись"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGED_DATE",
                            "Системная дата изменени записи"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGED_USER",
                            "Пользователь Изменивший запись"));


                    form.elements.add(new Element("photo", Element.ElementType.ePhoto, "photoDocuments", "Документы"));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto, "photoDamages", "Повреждения"));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto, "photoOther", "Другое"));

                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "SPECIALIST_SIGN",
                            "Подпись Комисcара"));
                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "CLIENT_SIGN",
                            "Подпись Клиента"));

                    form.elements.add(new Element("insured", Element.ElementType.eTextNum, "IIN",
                            "ИИН"));
                    form.elements.add(new Element("insured", Element.ElementType.eText, "FULLNAME",
                            "ФИО"));


//                    form.elements.add(new Element("", Element.ElementType.eText, "DOCUMENT_ID",
//                            "Системный код"));
//
//                    form.elements.add(new Element("general", Element.ElementType.eCombo, "DOCUMENT_TYPE",
//                            "Тип документа",
//                            "DCT_DOCUMENT_TYPE"));
//                    if (selectionTypes.equalsIgnoreCase("Car")) {
//                        //ArrayList<Element> header = new ArrayList<>();
//                        //ArrayList<Element> personalInfo = new ArrayList<>();
//                        //ArrayList<Element> carInfo = new ArrayList<>();
//                        //ArrayList<Element> additionalInfo1 = new ArrayList<>();
//
//                        /*
//                        form.elements.add(new Element(header, "header", "Заголовок"));
//
//                        form.elements.add(new Element(personalInfo, "personalInfo", "Личные данные"));
//
//                        form.elements.add(new Element(carInfo, "carInfo", "Данные по машине"));
//
//                        form.elements.add(new Element(additionalInfo1, "additionalInfo1", "Дополнительные данные"));
//
//                        */
//
//                        form.elements.add(new Element("general", Element.ElementType.eText, "city", "Город"));
//
//                        form.elements.add(new Element("general", Element.ElementType.eDate, "reportDate",
//                                "Дата составления акта"));
//
//                        form.elements.add(new Element("general", Element.ElementType.eText, "employee",
//                                "представитель АО «СК «Сентрас Иншуранс» - ФИО"));
//
//                        form.elements.add(new Element("general", Element.ElementType.eText, "client",
//                                "клиента - ФИО"));
//
//                        form.elements.add(new Element("general", Element.ElementType.eText, "address",
//                                "по адресу"));
//
//                        form.elements.add(new Element("personal", Element.ElementType.eText, "owner", "Владелец"));
//
//                        form.descriptionFields.add("owner");
//
//                        form.elements.add(new Element("personal", Element.ElementType.eText, "contactInfo",
//                                "Контактные данные"));
//                        /*
//
//                        carInfo.add(new Element(Element.ElementType.eText, "certificateOfRegistration",
//                                "Свидетельство о регистрации"));
//
//                        carInfo.add(new Element(Element.ElementType.eText, "autoBrand", "Марка Модель"));
//
//                        form.descriptionFields.add("autoBrand");
//
//                        carInfo.add(new Element(Element.ElementType.eText, "plateNumber",
//                                "Регистрационный номер"));
//
//                        carInfo.add(new Element(Element.ElementType.eText, "yearOfManufacture",
//                                "Год выпуска"));
//
//                        carInfo.add(new Element(Element.ElementType.eText, "engineCapacity",
//                                "Объем двигателя"));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя",
//                                new String[]{"Бензин", "Дизель", "Гибрид", "Газ"}));
//
//
//                        carInfo.add(new Element(Element.ElementType.eText, "bodyNumber",
//                                "Номер кузова"));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "bodyType", "Тип кузова",
//                                new String[]{"Седан", "Купе", "Универсал", "Хэтчбэк", "Джип", "Пикап", "Минивэн", "Автобус",
//                                        "Иное"}));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "numberOfDoors", "Количество дверей",
//                                new String[]{"2-дверная", "3-дверная", "4-дверная", "5-дверная"}));
//
//                        carInfo.add(new Element(Element.ElementType.eText, "bodyColor",
//                                "Цвет кузова"));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "gearboxType", "Тип КПП",
//                                new String[]{"автомат", "типтроник", "вариатор", "механика"}));
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "driveType", "Тип привода",
//                                new String[]{"передний", "задний", "полный"}));
//
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "steering", "Расположение руля",
//                                new String[]{"правый", "левый"}));
//
//
//                        carInfo.add(new Element(Element.ElementType.eText, "Mileage",
//                                "Пробег (километры, мили)"));
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "interior", "Отделка салона",
//                                new String[]{"кожаный", "велюровый", "синтетический"}));
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "headlights", "Фары",
//                                new String[]{"ксенон", "би-ксенон", "галоген"}));
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "fogLights", "Противотуманные фары",
//                                new String[]{"имеются", "отсутствуют"}));
//
//                        carInfo.add(new Element(Element.ElementType.eRadio, "wheels", "Диски колес",
//                                new String[]{"металлические", "легкосплавные"}));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "alarm", "Сигнализация",
//                                new String[]{"заводская", "отсутствует", "самостоятельно установленная"}));
//
//                        carInfo.add(new Element(Element.ElementType.eCombo, "storage", "Хранение",
//                                new String[]{"охраняемая стоянка", "частная территория", "гараж", "подземный паркинг", "двор"}));
//
//                        additionalInfo1.add(new Element(Element.ElementType.eText, "additionalInstallations",
//                                "Кроме вышеперечисленного на ТС дополнительно установлено следующее:"));
//
//                        additionalInfo1.add(new Element(Element.ElementType.eRadio, "conditionOfVehicle", "Состояние ТС",
//                                new String[]{"новое", "бывшее в употреблении"}));
//
//
//                        additionalInfo1.add(new Element(Element.ElementType.eText, "existingDamage",
//                                "Имеющиеся повреждения"));
//
//
//                        additionalInfo1.add(new Element(Element.ElementType.eCombo, "basisOfAssessment", "Основание",
//                                new String[]{"рыночная", "балансовая", "договор купли-продажи", "счет-справка"}));
//
//
//                        additionalInfo1.add(new Element(Element.ElementType.eText, "actualValue",
//                                "Действительная стоимость"));
//
//                        //signatures.add(new Element(Element.ElementType.eText, "fullName", "ФИО"));
//                        form.elements.add(new Element(Element.ElementType.eSignature, "signature", "Подпись Клиента"));
//                        */
//                    } else {
//                        /*
//                        ArrayList<Element> generalInfo1 = new ArrayList<>();
//                        ArrayList<Element> carInfo = new ArrayList<>();
//                        ArrayList<Element> regularSet = new ArrayList<>();
//                        ArrayList<Element> damageInfo = new ArrayList<>();
//                        ArrayList<Element> additionEquipment = new ArrayList<>();
//                        ArrayList<Element> alarmSystem = new ArrayList<>();
//                        */
//
//                        form.elements.add(new Element("general", Element.ElementType.eText, "numberOfPolice",
//                                "№"));
//                        form.elements.add(new Element("general", Element.ElementType.eDate, "date",
//                                "от"));
//
//                        form.elements.add(new Element("car", Element.ElementType.eText, "model", "Марка/модель ТС"));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "productionDate", "Год выпуска"));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "VIN", "VIN (№ кузова/шасси)"));
//                        form.elements.add(new Element("car", Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя ",
//                                new String[]{"бензиновый", "электродвигатель"}));
//
//                        form.elements.add(new Element("car", Element.ElementType.eText, "engineVolume", "Объем двигателя"));
//                        form.elements.add(new Element("car", Element.ElementType.eRadio, "driveType", "Тип привода",
//                                new String[]{"передний", "задний"}));
//                        form.elements.add(new Element("car", Element.ElementType.eCombo, "gearboxType", "Тип КПП",
//                                new String[]{"автомат", "типтроник", "вариатор", "механика"}));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "plateNumber", "Гос.рег.номер"));
//
//                        form.elements.add(new Element("car", Element.ElementType.eText, "mileage", "Пробег (на дату осмотра)миль/км"));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "owner", "Владелец  ТС:"));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "registration",
//                                "Свидетельство о регистрации ТС:"));
//                        form.elements.add(new Element("car", Element.ElementType.eText, "series", "Серия №"));
//
//                        form.elements.add(new Element("car", Element.ElementType.eText, "issued", "Выдан:"));
//
//                        form.elements
//                                .add(new Element("extra", Element.ElementType.eText, "name", "Название"));
//                        form.elements
//                                .add(new Element("extra", Element.ElementType.eText, "price", "Стоимость"));
//
//                        if (selectionTypes.equalsIgnoreCase("Bike")) {
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "windshield",
//                                    "Стекло ветровое"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "microcar",
//                                    "Мотоколяска "));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "motokofr",
//                                    "Мотокофр"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "motobagazhnik",
//                                    "Мотобагажник "));
//                            form.elements.add(new Element("car", Element.ElementType.eRadio, "headlights",
//                                    "Фары",
//                                    new String[]{"противотуманные", "ксеноновые"}));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "radio",
//                                    "Радио/музыкальное оборудование (автомагнитола)"));
//                            form.elements.add(new Element("car", Element.ElementType.eText, "rims",
//                                    "Покрышки (фирма,модель)"));
//                            form.elements.add(new Element("car", Element.ElementType.eText, "other",
//                                    "Другое штатное заводское оборудование:"));
//                            VehicleDamageView.carType = "Bike";
//                        } else if (selectionTypes.equalsIgnoreCase("Bus") ||
//                                selectionTypes.equalsIgnoreCase("Truck")) {
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "hatch", "люк"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "fogHeadlights",
//                                    "противотуманные фарые"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "xenon",
//                                    "ксеноновые фары"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "airConditioning",
//                                    "кондиционер"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "climateControl;",
//                                    "климат-контроль"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "centralLocking;",
//                                    "центральный замок"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "navigationSystem;",
//                                    "навигационная система"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "headlightWasher",
//                                    "омыватель фар"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "wiper",
//                                    "обогреватель щеток стеклоочистителя"));
//                            form.elements.add(new Element("car", Element.ElementType.eBoolean, "windshield",
//                                    "Стекло ветровое"));
//
//                            form.elements.add(new Element("car", Element.ElementType.eCombo, "sensors",
//                                    "Датчик парковки",
//                                    new String[]{"задний", "передний", "дождя", "света"}));
//                            form.elements.add(new Element("car", Element.ElementType.eRadio, "interior",
//                                    "Салон", new String[]{"ткань", "синтетика", "кожа"}));
//                            form.elements.add(new Element("car", Element.ElementType.eText, "bodyPaint", "Цвет кузова"));
//                            form.elements.add(new Element("car", Element.ElementType.eRadio, "tuning",
//                                    "Тюнинг", new String[]{"есть", "нет"}));
//                            form.elements.add(new Element("car", Element.ElementType.eText, "tuning",
//                                    "наименование"));
//                            form.elements.add(new Element("car", Element.ElementType.eRadio, "electricDrive",
//                                    "Электропривод", new String[]{"зеркала", "стеклоподъемники",
//                                    "сиденья"}));
//                            form.elements.add(new Element("car", Element.ElementType.eRadio, "electricHeater",
//                                    "Электроподогрев", new String[]{"зеркала", "сиденья"}));
//                            form.elements.add(new Element("car", Element.ElementType.eCombo, "equipment",
//                                    "Электроподогрев", new String[]{"заводская магнитола",
//                                    "заводской CD-чейнджер", "Встр. динамики"}));
//                            form.elements.add(new Element("car", Element.ElementType.eCombo, "rims",
//                                    "Колесные диски", new String[]{"Литые", "Стальные",
//                                    "Колпаки", "Резина"}));
//                            form.elements.add(new Element("car", Element.ElementType.eText, "otherEquipment",
//                                    "Другое оборудование"));
//                            if (selectionTypes.equalsIgnoreCase("Bus")) {
//                                VehicleDamageView.carType = "Bus";
//
//
//                            } else if (selectionTypes.equalsIgnoreCase(("Truck"))) {
//                                VehicleDamageView.carType = "Truck";
//                            }
//                        }
//
//                        /*
//                        damageInfo.add(new Element(Element.ElementType.ePlan, "damagePlan",
//                                "План повреждений"));
//                        regularSet.add(new Element(Element.ElementType.eText, "keys", "Количество оригинальных  комплектов  ключей у Страхователя"));
//                        regularSet.add(new Element(Element.ElementType.eText, "photos", "Количество фотографий ТС"));
//                        regularSet.add(new Element(Element.ElementType.eText, "specialNotes:", "Особые отметки:"));
//                        damageInfo.add(new Element(Element.ElementType.eText, "descriptionOfDamages",
//                                "Описание повреждений"));
//                        alarmSystem.add(new Element(Element.ElementType.eText, "immobiliser",
//                                "Иммобилайзер"));
//                        alarmSystem.add(new Element(Element.ElementType.eText, "antiTheft",
//                                "Электронная противоугонная система"));
//                        alarmSystem.add(new Element(Element.ElementType.eText, "gps",
//                                "Радиопоисковая система"));
//
//
//                        form.elements.add(new Element(Element.ElementType.eText, "initiatorName",
//                                "Ф.И.О. лица инициировавшего предстраховой осмотр ТС "));
//
//
//                        form.elements.add(new Element(Element.ElementType.eDateTime, "from",
//                                "с"));
//                        form.elements.add(new Element(Element.ElementType.eDateTime, "finishTime",
//                                "Время завершения осмотра"));
//
//                        form.elements.add(new Element(Element.ElementType.eSignature, "signatureClient", "Представитель страхователя"));
//                        form.elements.add(new Element(Element.ElementType.eSignature, "signatureEmployee", "Представитель Страховщика"));
//                        */
//                    }
////
                    break;

                case ("incident"):

                    form.elements.add(new Element("", Element.ElementType.eText, "DOCUMENT_ID",
                            "Системный код"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "DOCUMENT_TYPE",
                            "Тип документа",
                            "DCT_DOCUMENT_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLAIM_REGID",
                            "Номер дела"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "CLAIM_TYPE",
                            "Тип дела",
                            "DCT_CLAIM_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eDateTime, "CLAIM_STARTED",
                            "Дата регистрации дела"));

                    form.elements.add(new Element("", Element.ElementType.eCombo, "CAUSE_ID",
                            "Тип урегулирования",
                            "DCT_CAUSE_ID"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "POLICY_REG",
                            "№ Договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_DATE_GIVEN",
                            "Дата выдачи договора"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_INSR_BEGIN",
                            "начало действия"));

                    form.elements.add(new Element("general", Element.ElementType.eDate, "POLICY_INSR_END",
                            "конец действия"));


                    form.elements.add(new Element("general", Element.ElementType.eCombo, "INSR_TYPE",
                            "Код продукта страхования",
                            "DCT_INSR_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО Страхователя"));

                    form.elements.add(new Element("general", Element.ElementType.eDateTime, "EVENT_DATE",
                            "Дата страх. случая"));

                    form.elements.add(new Element("general", Element.ElementType.eCombo, "EVENT_TYPE",
                            "Причина/Событие страх. Случая", "DCT_EVENT_TYPE"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "EVENT_PLACE",
                            "Место события"));
                    form.elements.add(new Element("general", Element.ElementType.eText, "EVENT_DESCRIPTION",
                            "Описание события"));

                    form.elements.add(new Element("general", Element.ElementType.eTextNum, "INITIAL_SUM",
                            "Предварительная сумма (ущерба для страховых случае, стоимости авто для предварительного страхового осмотра)"));

                    form.elements.add(new Element("", Element.ElementType.eText, "REGISTRATION_DATE",
                            "Системное/Серверное время создания записи"));
                    form.elements.add(new Element("", Element.ElementType.eText, "USERNAME",
                            "Пользователь создавший запись (Login)"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGED_BY",
                            "Пользователь изменивший запись (Login)"));
                    form.elements.add(new Element("", Element.ElementType.eText, "CHANGE_DATE",
                            "Системное/Серверное время изменения записи"));

                    form.elements.add(new Element("general", Element.ElementType.eText, "OPERATOR_NAME",
                            "Имя оператора кол-центра"));
                    form.elements.add(new Element("", Element.ElementType.eTextNum, "SEND_SMS",
                            "На какой номер АК было отправлено уведомление о страх. Случае"));
                    form.elements.add(new Element("general", Element.ElementType.eTextNum, "CLAIMANT_PHONE_NO",
                            "Номер телефона застрахованного"));
                    form.elements.add(new Element("", Element.ElementType.eText, "SMS_PROVIDER",
                            "СМС провайдер"));
                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "SPECIALIST_SIGN",
                            "Подпись Комисcара"));
                    form.elements.add(new Element("signature", Element.ElementType.eSignature, "CLIENT_SIGN",
                            "Подпись Клиента"));

                    form.elements.add(new Element("photo", Element.ElementType.ePhoto, "photoDocuments", "Фото документов",
                            new String[] {"Уд.личности сторона 1",
                                    "Уд. личности сторона 2",
                                    "Тех.паспорт сторона 1",
                                    "Тех.паспорт сторона 2",
                                    "Страховой полис",
                                    "Акт осмотра"
                            }));
                    form.elements.add(new Element("photo", Element.ElementType.ePhoto, "photoPlace", "Фото места ДТП",
                            new String[] {
                                    "Общий вид ДТП",
                                    "Общий вид ДТП слева 45 град",
                                    "Общий вид ДТП справа 45 град"
                            }));


                    //form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "DOCUMENT_ID",
                    //        "Ссылка на основную запись в таблице общие данные"));

                    //form.elements.add(new Element("additionalInfo", Element.ElementType.eCombo, "QUESTION_ID",
                    //        "Системный код/счетчик дела (sequence)" +
                    //                "FireBase"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_CODE",
                            "Код вопроса"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_DESC",
                            "Описание вопроса"));

                    form.elements.add(new Element("", Element.ElementType.eText, "QUESTION_ANSWER_TYPE",
                            "Тип ответа"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eCombo, "QUESTION_ANSWER",
                            "Ответ на вопрос", "DCT_QUESTION_ANSWER"));


                    form.elements.add(new Element("attachments", Element.ElementType.eCombo, "ATTACHMENT_TYPE",
                            "Тип документа", "DCT_ATTACHMENT_TYPE"));
                    form.elements.add(new Element("attachments", Element.ElementType.eText, "ATTACHMENT_COMMENTS",
                            "Комментарии по документу"));





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


                    break;
                default:
                    break;
            }
            //now insert values from "input" field, posted by INSIS
            form.applyInput(form.elements);
        }

    }

}
