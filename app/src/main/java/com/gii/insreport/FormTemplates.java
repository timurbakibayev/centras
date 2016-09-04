package com.gii.insreport;

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

                        form.elements.add(new Element("general",Element.ElementType.eText, "city", "Город"));

                        form.elements.add(new Element("general",Element.ElementType.eDate, "reportDate",
                                "Дата составления акта"));

                        form.elements.add(new Element("general",Element.ElementType.eText, "employee",
                                "представитель АО «СК «Сентрас Иншуранс» - ФИО"));

                        form.elements.add(new Element("general",Element.ElementType.eText, "client",
                                "клиента - ФИО"));

                        form.elements.add(new Element("general",Element.ElementType.eText, "address",
                                "по адресу"));

                        form.elements.add(new Element("personal",Element.ElementType.eText, "owner", "Владелец"));

                        form.descriptionFields.add("owner");

                        form.elements.add(new Element("personal",Element.ElementType.eText, "contactInfo",
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

                        form.elements.add(new Element("general",Element.ElementType.eText, "numberOfPolice",
                                "№"));
                        form.elements.add(new Element("general",Element.ElementType.eDate, "date",
                                "от"));

                        form.elements.add(new Element("car",Element.ElementType.eText, "model", "Марка/модель ТС"));
                        form.elements.add(new Element("car",Element.ElementType.eText, "productionDate", "Год выпуска"));
                        form.elements.add(new Element("car",Element.ElementType.eText, "VIN", "VIN (№ кузова/шасси)"));
                        form.elements.add(new Element("car",Element.ElementType.eCombo, "typeOfEngine", "Тип двигателя ",
                                new String[]{"бензиновый", "электродвигатель"}));

                        form.elements.add(new Element("car",Element.ElementType.eText, "engineVolume", "Объем двигателя"));
                        form.elements.add(new Element("car",Element.ElementType.eRadio, "driveType", "Тип привода",
                                new String[]{"передний", "задний"}));
                        form.elements.add(new Element("car",Element.ElementType.eCombo, "gearboxType", "Тип КПП",
                                new String[]{"автомат", "типтроник", "вариатор", "механика"}));
                        form.elements.add(new Element("car",Element.ElementType.eText, "plateNumber", "Гос.рег.номер"));

                        form.elements.add(new Element("car",Element.ElementType.eText, "mileage", "Пробег (на дату осмотра)миль/км"));
                        form.elements.add(new Element("car",Element.ElementType.eText, "owner", "Владелец  ТС:"));
                        form.elements.add(new Element("car",Element.ElementType.eText, "registration",
                                "Свидетельство о регистрации ТС:"));
                        form.elements.add(new Element("car",Element.ElementType.eText, "series", "Серия №"));

                        form.elements.add(new Element("car",Element.ElementType.eText, "issued", "Выдан:"));

                        form.elements
                                .add(new Element("extra",Element.ElementType.eText, "name", "Название"));
                        form.elements
                                .add(new Element("extra",Element.ElementType.eText, "price", "Стоимость"));

                        if (selectionTypes.equalsIgnoreCase("Bike")) {
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "microcar",
                                    "Мотоколяска "));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "motokofr",
                                    "Мотокофр"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "motobagazhnik",
                                    "Мотобагажник "));
                            form.elements.add(new Element("car",Element.ElementType.eRadio, "headlights",
                                    "Фары",
                                    new String[]{"противотуманные", "ксеноновые"}));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "radio",
                                    "Радио/музыкальное оборудование (автомагнитола)"));
                            form.elements.add(new Element("car",Element.ElementType.eText, "rims",
                                    "Покрышки (фирма,модель)"));
                            form.elements.add(new Element("car",Element.ElementType.eText, "other",
                                    "Другое штатное заводское оборудование:"));
                            VehicleDamageView.carType = "Bike";
                        } else if (selectionTypes.equalsIgnoreCase("Bus") ||
                                selectionTypes.equalsIgnoreCase("Truck")) {
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "hatch", "люк"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "fogHeadlights",
                                    "противотуманные фарые"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "xenon",
                                    "ксеноновые фары"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "airConditioning",
                                    "кондиционер"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "climateControl;",
                                    "климат-контроль"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "centralLocking;",
                                    "центральный замок"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "navigationSystem;",
                                    "навигационная система"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "headlightWasher",
                                    "омыватель фар"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "wiper",
                                    "обогреватель щеток стеклоочистителя"));
                            form.elements.add(new Element("car",Element.ElementType.eBoolean, "windshield",
                                    "Стекло ветровое"));

                            form.elements.add(new Element("car",Element.ElementType.eCombo, "sensors",
                                    "Датчик парковки",
                                    new String[]{"задний", "передний", "дождя", "света"}));
                            form.elements.add(new Element("car",Element.ElementType.eRadio, "interior",
                                    "Салон", new String[]{"ткань", "синтетика", "кожа"}));
                            form.elements.add(new Element("car",Element.ElementType.eText, "bodyPaint", "Цвет кузова"));
                            form.elements.add(new Element("car",Element.ElementType.eRadio, "tuning",
                                    "Тюнинг", new String[]{"есть", "нет"}));
                            form.elements.add(new Element("car",Element.ElementType.eText, "tuning",
                                    "наименование"));
                            form.elements.add(new Element("car",Element.ElementType.eRadio, "electricDrive",
                                    "Электропривод", new String[]{"зеркала", "стеклоподъемники",
                                    "сиденья"}));
                            form.elements.add(new Element("car",Element.ElementType.eRadio, "electricHeater",
                                    "Электроподогрев", new String[]{"зеркала", "сиденья"}));
                            form.elements.add(new Element("car",Element.ElementType.eCombo, "equipment",
                                    "Электроподогрев", new String[]{"заводская магнитола",
                                    "заводской CD-чейнджер", "Встр. динамики"}));
                            form.elements.add(new Element("car",Element.ElementType.eCombo, "rims",
                                    "Колесные диски", new String[]{"Литые", "Стальные",
                                    "Колпаки", "Резина"}));
                            form.elements.add(new Element("car",Element.ElementType.eText, "otherEquipment",
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

                    form.elements.add(new Element("general",Element.ElementType.eText, "DOCUMENT_ID",
                            "Системный код"));

                    form.elements.add(new Element("general",Element.ElementType.eCombo, "DOCUMENT_TYPE",
                            "Тип документа",
                            "DCT_DOCUMENT_TYPE"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "CLAIM_REGID",
                            "Номер дела"));

                    form.elements.add(new Element("general",Element.ElementType.eCombo, "DCT_CLAIM_TYPE",
                            "Тип дела",
                            new String[]{"Прочее имущество третьих лиц (ОГПО ВТС)", "Имущество - транспортное средство",
                                    "Ответственность", "Неимущественные риски", "Финансовые потери",
                                    "Страхование займов", "ОГПО владельцев опасных объектов",
                                    "Обязательное экологическое страхование", "ДГПО водного ТС",
                                    "ДГПО воздушного транспорта", "ОГПО аудиторов", "ГПО владельцев ж/д",
                                    "Автокаско", "Добровольное страхование воздушного транспорта",
                                    "Добровольное страхование водного транспорта", "НС", "Имущество",
                                    "Грузы", "Добровольное страхование жд транспорта", "СМР",
                                    "ДГПО ВТС", "Ответственность работодателя", "Ответственность перевозчика"}));

                    form.elements.add(new Element("general",Element.ElementType.eDateTime, "CLAIM_STARTED",
                            "Дата регистрации дела"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "POLICY_REG",
                            "№ Договора"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "POLICY_DATE_GIVEN",
                            "Дата выдачи договора"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "POLICY_INSR_BEGIN",
                            "Начало действия"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "POLICY_INSR_END",
                            "Конец действия"));

                    form.elements.add(new Element("general",Element.ElementType.eCombo, "INSR_TYPE",
                            "Код продукта страхования",
                            "DCT_INSR_TYPE"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО Страхователя"));

                    form.elements.add(new Element("general",Element.ElementType.eText, "CLIENT_NAME",
                            "ФИО Страхователя"));

                    form.elements.add(new Element("general",Element.ElementType.eDateTime, "EVENT_DATE",
                            "Дата страх. случая"));

                    form.elements.add(new Element("general",Element.ElementType.eCombo, "DCT_EVENT_TYPE",
                            "Причина/Событие страх. Случая",
                            new String[]{"Пожар", "Стихийные бедствия", "Домашний несчастный случай",
                                    "Транспортное происшествие", "Болезнь", "Кража отдельных частей",
                                    "Удар током", "Несчастный случай",
                                    "Аварии водопроводных, канализационных, отопительных систем",
                                    "Нападение животных",
                                    "Падение летательных аппаратов и/или их обломков",
                                    "Грабёж", "Вандализм", "Техническая неисправность",
                                    "Буря", "Паводок", "Наводнение", "Кража со взломом",
                                    "Проливной дождь", "Повреждения причиненные путем затопления",
                                    "Повреждения причиненные путем падения предмета с высоты",
                                    "Ущерб, нанесенный имуществу третьих лиц",
                                    "авария при проведении строительно-монтажных работ",
                                    "Нарушения законодательства  при  сделках купли-продажи",
                                    "Аварии водопроводных, канализационных, отопительных систем",
                                    "Наезд наземных транспортных средств или самодвижущихся машин",
                                    "Битье стекол",
                                    "Вступление в законную силу решения суда о" +
                                            " возмещении вреда, причиненного страхователем",
                                    "Сель", "Взрыв", "Распоряжение суда", "Признание страхователем" +
                                    " требований аудируемых субъектов о возмещении вреда" +
                                    " обоснованными и согласие страховщика с признанием" +
                                    " требований страхователя.",
                                    "Обвал", "Провал под лед", "Землетрясение",
                                    "Повреждение, уничтожение или кража ДО",
                                    "Иные непредвиденные события",
                                    "Разрушение транспортного средства третьими лицами",
                                    "Самовольное падение предметов",
                                    "Падение или переворачивание ТС",
                                    "Падение ТС в воду",
                                    "Буря", "ненадлежащее оформление юридических документов,подтверждающих право собственности",
                                    "Грязевой поток", "Ураган", "Снегопад", "Самовозгорание",
                                    "Передача  в собственность залогового недвижимого имущества",
                                    "Приобретение собственности у лица, относящегося к «группе риска»",
                                    "Предъявление прав на недвижимость «временно» выписанными (армия, учеба и т.д.) лицами",
                                    "Приобретение собственности, находящейся под арестом, запретом или залогом",
                                    "Нападение животных/пчел/змей или другое повреждение, вызванное любым видом флоры/фауны",
                                    "Смерть", "Кража со взломом", "Неуплата Заемщиком очередного взноса в сроки",
                                    "Столкновение с  воздушными средствами", "Град", "Удар молнии",
                                    "Столкновение", "Дорожно-транспортное происшествие",
                                    "Опрокидывание транспортного средства", "Столкновение с подвижными или неподвижными объектами",
                                    "Оползень", "Медицинские показания", "снежная буря", "Затопление",
                                    "Обвал", "Столкновение с наземными средствами", "Молния",
                                    "Противоправные действия третьих лиц", "Сообщение от клиента/ застрахованного",
                                    "Транспортная авария", "Авария при проведении строительно-монтажных работ",
                                    "Иные непредвиденные события",
                                    "Ошибки, допущенные при производстве строительно-монтажных и пуско-наладочных операций",
                                    "Ошибки, допущенные при выполнении гарантийных обязательств",
                                    "Происшествие на воде",
                                    "Услуги семейного доктора",
                                    "Спланированный ущерб",
                                    "Медицинские расходы за период беременности",
                                    "Медицинские расходы за роды",
                                    "Непредвиденный выброс нефти, газа",
                                    "Конструктивные ошибки и недостатки в оборудовании",
                                    "Посадка на мель",
                                    "Водный ущерб",
                                    "Крушение",
                                    "Сход с рельсов",
                                    "Удар внешнего объекта",
                                    "Разгрузка в порту или на месте крушения",
                                    "Убытки и взносы по общей аварии",
                                    "Расходы по спасению груза",
                                    "Расходы по уменьшению ущерба",
                                    "Смытие волной или выбрасывание за борт",
                                    "Проникновение забортной воды",
                                    "Разгрузка / погрузка грузов",
                                    "Расходы для услуг аварийных комиссаров и экспертов",
                                    "Транспорт отломок трансп.ср-в",
                                    "Подмочка или затопление грунтовыми водами",
                                    "Авария водопроводной, отопительной, канализационной систем",
                                    "Неисправность отопительной системы",
                                    "Неисправность канализационной системы",
                                    "Преднамеренный ущерб или уничтожение груза",
                                    "Преступления в компьютерной сфере",
                                    "Транспортная авария",
                                    "Телесное увреждение причинено оторвавшимися компонентами движущегося трансп.ср-ва",
                                    "Воровство, угон,разбой, кража",
                                    "Хулиганство,грабеж,воровство",
                                    "Буря,ураган,шторм,град",
                                    "Сель,оползень,обвал",
                                    "Наводнение",
                                    "Неожиданный удар,внешний эффект (неисправность моторного транспорта)",
                                    "Удар молнии",
                                    "Обледенение",
                                    "Ущерб в связи с неисправностью трубопроводов",
                                    "Нагревательная или канализационная система",
                                    "Другие неожиданные события",
                                    "Действия третьих лиц",
                                    "Поврежденные отопительная, осветительная системы и электротехн.оборудование",
                                    "Поврежденные машины и оборудование",
                                    "Неизвестные причины",
                                    "Сель",
                                    "Смерч",
                                    "Ливень",
                                    "Взрыв паровых котлов,газовых камер,газопроводов,оборудования,машин и др.",
                                    "Разбой",
                                    "Повреждения в отопительных системах",
                                    "Повреждения в осветительной системе",
                                    "Повреждения в электрической системе",
                                    "Хулиганство",
                                    "Другие неправомерные действия третьих лиц",
                                    "Технич.неисправность трансп.средства",
                                    "Происшествие с возд.судном",
                                    "Столкновение с оборудованием и сооружениями гаваней, доков",
                                    "Травма",
                                    "Авария",
                                    "Невнимательность",
                                    "Повреждение",
                                    "Взрыв",
                                    "Удар молнии",
                                    "Взрыв газа",
                                    "Вихрь",
                                    "Сель",
                                    "Горный обвал",
                                    "Умышленное повреждение/уничтожение имущества",
                                    "Неисправность летательных аппаратов",
                                    "Попадание камня на стекло, оптику",
                                    "Кража легкосъемных деталей",
                                    "Взрыв паровых котлов",
                                    "Взрыв газохранилищ",
                                    "Взрыв газопроводов",
                                    "Взрыв машин и аппаратов",
                                    "Затопление водопроводными системами",
                                    "Затопление канализационными системами",
                                    "Затопление отопительными системами",
                                    "Проникновение воды из соседних помещений",
                                    "Наступление страхового случая",
                                    "Землетрясение",
                                    "Наводнение",
                                    "Шторм",
                                    "Повреждение в результате аварии водопроводной, отопительной или канализационной систем",
                                    "Совершение сделки несовершеннолетним, достигшим 14 лет",
                                    "Совершение сделки лицом, признаным недееспособным",
                                    "Совершение сделки лицом, ограниченным судом",
                                    "Совершение сделки лицом, имеющим недостаточные полномочия",
                                    "Совершение сделки без согласия одного из супругов",
                                    "Появление после совершения сделки не указанных в договоре лиц",
                                    "Продажа имущества по доверенности в случае смерти владельца",
                                    "Продажа  имущества, не принадлежащего продавцу",
                                    "Подделки, подлога документов",
                                    "Совершения сделки юридическим лицом в противоречии с целями деятельности",
                                    "Авария канализационной системы",
                                    "Падение на мототранспорт предметов извне",
                                    "Последствия пожаротушения",
                                    "Шторм",
                                    "Град",
                                    "Бой оконных стекол, зеркал, витрин",
                                    "Частичное повреждение имущества",
                                    "Полная гибель имущества",
                                    "Инвалидность 1 группы",
                                    "Инвалидность 2 группы",
                                    "Инвалидность 3 группы",
                                    "Временная утрата трудоспособности",
                                    "Самовозгорание",
                                    "Ураган",
                                    "Буря",
                                    "Смерч",
                                    "Касание грунта",
                                    "Посадка на мель",
                                    "Кража",
                                    "Угон",
                                    "Грабеж",
                                    "Разбой",
                                    "Пиратство",
                                    "Поджог",
                                    "Иные противоправные действия 3-х лиц",
                                    "Пропажа без вести",
                                    "Случайное повреждение при погрузке",
                                    "Случайное повреждение при разгрузке",
                                    "Падение груза на борт водного транспорта",
                                    "Извержение вулкана",
                                    "Падение летательных аппаратов или их обломков и предметов из них",
                                    "Преднамеренный выброс за борт какого-либо имущества с целью избежания большего убытка",
                                    "Опрокидывание",
                                    "Столкновение с плавучим объектом",
                                    "Столкновение с неподвижным объектом",
                                    "Проседание грунта",
                                    "Действие подземного огня",
                                    "Крушение",
                                    "Авария",
                                    "Столкновение",
                                    "Сход с рельсов",
                                    "Обвал моста",
                                    "Разрушение моста",
                                    "Обвал туннеля",
                                    "Разрушение туннеля",
                                    "Тайфун",
                                    "Ливень",
                                    "Катастрофа",
                                    "Авиационное происшествие",
                                    "Инцидент в полете",
                                    "Смерть",
                                    "Признание Застрахованного безвестно отстсутствующим",
                                    "Признание Застрахованного умершим",
                                    "Увольнение по сокращению",
                                    "Принудительная ликвидация организации",
                                    "Остановка производства",
                                    "Отсутствие выручки у Страхователя",
                                    "Причинение вреда жизни/здоровью третьих лиц",
                                    "Причинение вреда имуществу третьих лиц",
                                    "Причинение вреда жизни/здоровью пассажиров",
                                    "Полная гибель багажа",
                                    "Частичная гибель багажа",
                                    "Повреждение багажа",
                                    "Полная гибель груза",
                                    "Частичная гибель груза",
                                    "Повреждение груза",
                                    "Причинение вреда жизни/здоровью туриста",
                                    "Причинение вреда имущественным интересам туриста",
                                    "Категория ребенок-инвалид",
                                    "Увечье, травма или иное повреждение здоровья без установления инвалидности",
                                    "Причинение вреда жизни третьих лиц",
                                    "Причинение вреда здоровью третьих лиц",
                                    "Причинение вреда окружающей среде",
                                    "Залив водой",
                                    "Общая авария",
                                    "Потеря работы",
                                    "Неисполнение договорных обязательств контрагентом Страхователя",
                                    "Неоплата товара, работы или услуги",
                                    "Непоставка (задержка) товара или продукции",
                                    "Невыполнение работы",
                                    "Потеря рыночной стоимости",
                                    "Перерыв в предпринимательской деятельности",
                                    "Участие Страхователя в инвестиционной деятельности",
                                    "Засуха",
                                    "Пыльные/песчаные бури",
                                    "Экстремальные температуры",
                                    "Грозы",
                                    "Торнадо",
                                    "Сильный дождь и снег",
                                    "Сильные ветры",
                                    "Волны тепла",
                                    "Другое",
                                    "Непредвиденный выброс нефти",
                                    "Непредвиденный выброс газа",
                                    "Столкновение с наземными средствами транспорта",
                                    "Столкновение с водными средствами транспорта",
                                    "Столкновение с воздушными средствами транспорта",
                                    "Столкновение с якорными цепями",
                                    "Столкновение с буями",
                                    "Столкновение с рыболовными сетями",
                                    "Выход скважины из-под контроля",
                                    "Кража со взломом",
                                    "Образование кратера",
                                    "Образование грифонов",
                                    "Образование воронок",
                                    "Прекращение подачи электроэнергии из энергосети",
                                    "Приобретение необходимых материалов",
                                    "Аренда специального оборудования",
                                    "Услуги сторонних лиц по восстановлению контроля над скважинами или тушении пожара на скважине",
                                    "Выполнение буровых и иных работ, необходимых для восстановления контроля над скважиной",
                                    "Извлечение внутрискважинного оборудования",
                                    "Повторное бурение",
                                    "Причинение вреда имуществу пассажиров",
                                    "Оползень",
                                    "Причинение вреда грузовладельцу",
                                    "Просроченная задолжность",
                                    "Взрыв газа, употребляемого в бытовых целях",
                                    "Экстренная медицинская помощь бригадой скорой помощи",
                                    "Первичная консультация и лечение специалистами экстренной помощи",
                                    "Организация госпитализации",
                                    "Круглосуточная диспетчерская служба (в пределах 5 км. от границы города)",
                                    "Транспортировка Застрахованного в медицинское учреждение",
                                    "Консультации диспетчерской службы",
                                    "Организация визитов врача по вызову",
                                    "Организация вызова скорой помощи",
                                    "Запись на прием к специалистам",
                                    "Информация о медицинском персонале",
                                    "Прием семейного врача",
                                    "Проведение лабораторно-инструментальных методов исследования для установления диагноза",
                                    "Консультации и др. профессиональные услуги специалистов по экстренным показаниям",
                                    "Диагностические лабораторные и инструментальные исследования по экстренным показаниям",
                                    "Неисполнение/ненадлежащее исполнение Страхователем обязательств по договору",
                                    "Причинение вреда интересам Потерпевших (Учредителей)",
                                    "Причинение вреда окружающей среде",
                                    "Неуплата таможенных пошлин и налогов",
                                    "Причинение вреда имуществу",
                                    "Причинение вреда жизни/здоровью",
                                    "Повреждение, утрата, уничтожение груза/багажа",
                                    "Причинение вреда жизни/здоровью 3-х лиц, не состоящих со Страхователем в трудовых отношениях",
                                    "Причинение вреда имуществу 3-х лиц, не состоящих со Страхователем в трудовых отношениях",
                                    "Сбой в системах энергоснабжения",
                                    "Обвал/частичный обвал собственных и/или арендных строений или их части",
                                    "Ущерб от проведения погрузочно-разгрузочных работ",
                                    "Ущерб при перевозке",
                                    "Бандитизм",
                                    "Утечка из автоматической спринклерной системы пожаротушения",
                                    "Звуковой удар",
                                    "Гражданские волнения",
                                    "Массовые беспорядки",
                                    "Авария инженерных сетей",
                                    "Наезд движущейся техники",
                                    "Ошибка при монтаже",
                                    "Обрушение или повреждение объекта",
                                    "Камнепад",
                                    "Снежная лавина",
                                    "Травма",
                                    "Затопление",
                                    "Давление снежных масс",
                                    "Сход лавин",
                                    "Воздействие урагана",
                                    "Воздействие града",
                                    "Наезд транспортных средств",
                                    "Противоправные действия третьих лиц",
                                    "Недостача зерна",
                                    "Утрата зерна",
                                    "Повреждение зерна",
                                    "Порча зерна",
                                    "Ухудшение качества зерна",
                                    "Гибель ВС (полная или конструктивная)",
                                    "Пропажа ВС без вести",
                                    "Война, интервенция, боевые действия",
                                    "Забастовки, бунты, массовые беспорядки",
                                    "Террористические действия",
                                    "Злонамеренные действия, саботаж",
                                    "Конфискация, национализация, наложение ареста",
                                    "Гибель (утрата), недостача груза или почты",
                                    "Повреждение (порча) груза или почты",
                                    "Угон, незаконный захват",
                                    "Вступление в силу решения суда",
                                    "Признание Страхователем требования 3-х лиц о возмещении вреда",
                                    "Вызов скорой помощи",
                                    "Осмотр педиатра",
                                    "Осмотр невропатолога",
                                    "Осмотр ортопеда-хирурга",
                                    "Осмотр невропатолога (по показаниям)",
                                    "Осмотр офтальмолога",
                                    "Осмотр ортопеда-хирурга (по показаниям)",
                                    "Осмотр ЛОР-врача",
                                    "УЗИ головного мозга (по показаниям)",
                                    "Общий анализ крови",
                                    "Общий анализ мочи",
                                    "Анализ кала на я/г",
                                    "Массаж",
                                    "Физиолечение",
                                    "Госпитализация",
                                    "АКДС 1",
                                    "АКДС 2",
                                    "АКДС 3",
                                    "ВПП 1",
                                    "ВГВ 2",
                                    "ВПП 3",
                                    "ВПП 4",
                                    "Вакцина против пневмококковой инфекции",
                                    "Корь",
                                    "Эпидпаротит",
                                    "Коревая краснуха",
                                    "ВПП 2",
                                    "ВГВ 3",
                                    "По плану",
                                    "Травма",
                                    "Отравление",
                                    "Тепловой удар",
                                    "Ожог",
                                    "Обморожение",
                                    "Утопление",
                                    "Поражение молнией или электрическим током",
                                    "Падение с высоты",
                                    "Повреждения в результате контакта с животными",
                                    "Несчастный случай",
                                    "Обращение в Call-Center",
                                    "Амбулаторно-поликлиническая помощь",
                                    "Прием в поликлинике",
                                    "Контроль за ходом лечения в стационаре",
                                    "Прием узкого специалиста",
                                    "Лечебные манипуляции",
                                    "ИФА, ПЦР, РИФ",
                                    "Компьютерная томография",
                                    "Магнитно-резонансная томография",
                                    "Внутривенное лазерное облучение крови",
                                    "Стационарное лечение",
                                    "Стоматология",
                                    "Лекарства",
                                    "Вакцинация от гриппа",
                                    "Ущерб, нанесенный имуществу третьих лиц",
                                    "Ущерб, причиненный имуществу клиентов Страхователя",
                                    "Санаторно-курортное лечение",
                                    "Дневной стационар",
                                    "Услуги медицинской сестры на дому",
                                    "Стационарное лечение по экстренным показаниям",
                                    "Стационарное лечение по плановым показаниям",
                                    "Вред, нанесенный имуществу третьих лиц",
                                    "Дорожное происшествие",
                                    "Ущерб,причиненный выпадающ.объектамии/частьями трансп.ср-ва",
                                    "Повреждения воздушного судна",
                                    "Другое"
                            }));

                    form.elements.add(new Element("general",Element.ElementType.eText, "EVENT_PLACE",
                            "Место события"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "INITIAL_SUM",
                            "Предварительная сумма (ущерба для страховых случае, стоимости авто для предварительного страхового осмотра)"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "OPERATOR_NAME",
                            "Имя оператора кол-центра"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "SEND_SMS",
                            "На какой номер АК было отправлено уведомление о страх. Случае"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "CLAIMANT_PHONE_NO",
                            "Номер телефона застрахованного"));
                    form.elements.add(new Element("general",Element.ElementType.eText, "SMS_PROVIDER",
                            "СМС провайдер"));


                    form.elements.add(new Element("participant",Element.ElementType.eCombo, "DCT_PERSON_TYPE",
                            "Тип персоны", new String[]{
                            "Владелец",
                            "Пострадавший",
                            "Кредитор",
                            "Страховщик",
                            "Аварийный комисар",
                            "СТО",
                            "Медицинское учреждение",
                            "Клиент",
                            "Кредитор по полису",
                            "Страхователь",
                            "Страхователь-грузы",
                            "Владелец имущества",
                            "Заявитель",
                            "Застрахованный",
                            "Выгодоприобретатель",
                            "Эксперт",
                            "Застрахованный водитель",
                            "ЛПУ",
                            "Аджастер"
                    }));

                    form.elements.add(new Element("participant",Element.ElementType.eText, "PERSON_IIN",
                            "Иин персоны"));

                    form.elements.add(new Element("participant",Element.ElementType.eText, "GUILT_PERCENTAGE",
                            "% виновности"));
                    form.elements.add(new Element("participant",Element.ElementType.eText, "GUILTY_CONTRACT_NO",
                            "Номер договора другой страховой компании"));
                    form.elements.add(new Element("participant",Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
                            "Дата договора другой страховой компании"));
                    form.elements.add(new Element("participant",Element.ElementType.eCombo, "DCT_THIRD_PART_INSURER",
                            "страховая компания",
                            new String[]{
                                    "Cентрас Иншуранс",
                                    "Grandes",
                                    "Kaspi Страхование",
                                    "Kompetenz",
                                    "NOMAD LIFE",
                                    "Standard",
                                    "Альянс Полис",
                                    "Аманат",
                                    "Виктория",
                                    "Государственная аннуитетная компания",
                                    "Евразия",
                                    "Европейская Страховая Компания",
                                    "Казахмыс",
                                    "Казкоммерц-Life",
                                    "Казкоммерц-Полис",
                                    "КазЭкспортГарант",
                                    "Коммеск-?мір",
                                    "Лондон-Алматы",
                                    "Нефтяная страховая компания",
                                    "НОМАД Иншуранс",
                                    "Салем",
                                    "Халык-Life",
                                    "Халык-Казахинстрах",
                                    "Цесна Гарант"
                            }));

                    form.elements.add(new Element("object",Element.ElementType.eCombo, "DCT_OBJECT_TYPE",
                            "тип объекта",
                            new String[]{"Застрахованный",
                                    "Земледелие",
                                    "Автомобиль",
                                    "Имущество",
                                    "Страхование жизни",
                                    "Грузы",
                                    "Здания в процессе строительства",
                                    "Страхование групп",
                                    "Ответственность",
                                    "Дополнительное оборудование",
                                    "Воздушный транспорт",
                                    "Водный транспорт",
                                    "Железнодорожный транспорт"}));


                    form.elements.add(new Element("object",Element.ElementType.eCombo, "DCT_OBJECT_SUB_TYPE",
                            "подтип объекта",
                            new String[]{"Легковые  автомашины",
                                    "Трамваи, троллейбусы",
                                    "Тракторы (колесный)",
                                    "Спец. техника",
                                    "Грузовые",
                                    "Автобус более 16 мест",
                                    "Автобус до 16 мест",
                                    "Трактор (гусеничный)",
                                    "Мотоциклы, мотороллеры",
                                    "Автобус",
                                    "Прицепы, полуприцепы"}));

                    form.elements.add(new Element("object",Element.ElementType.eCombo, "DCT_OBJECT_PRODUCTION",
                            "марка",
                            new String[]{"test"}));
                    form.elements.add(new Element("object",Element.ElementType.eCombo, "DCT_OBJECT_MODEL",
                            "модель",
                            new String[]{"test"}));


                    form.elements.add(new Element("object",Element.ElementType.eText, "OBJECT_CHASSIS_NO_VIN",
                            "№ кузова, № шасси"));
                    form.elements.add(new Element("object",Element.ElementType.eText, "OBJECT_REGISRATION_NUMBER",
                            "Регистрационный номер объекта"));
                    form.elements.add(new Element("object",Element.ElementType.eText, "OBJECT_ENGINE_NO",
                            "Номер двигателя"));
                    form.elements.add(new Element("object",Element.ElementType.eText, "PRODUCTION_YEAR",
                            "Год производства"));
                    form.elements.add(new Element("object",Element.ElementType.eText, "PRODUCTION_MONTH",
                            "Месяц производства"));

                    form.elements.add(new Element("object",Element.ElementType.eText, "CAR_COLOUR",
                            "Цвет объекта"));


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
