package com.gii.insreport;

import java.util.Date;

/**
 * Created by Timur_hnimdvi on 07-Aug-16.
 */
public class FormTemplates {
    public static String selectionTypes = "";

    public static void applyTemplateForParticipants(Element element) {
        element.elements.add(new Element("participant", Element.ElementType.eText, "LAST_NAME",
                "Фамилия"));
        element.elements.add(new Element("participant", Element.ElementType.eText, "FIRST_NAME",
                "Имя"));
        element.elements.add(new Element("participant", Element.ElementType.eText, "MIDDLE_NAME",
                "Отчество"));

        element.elements.add(new Element("participant", Element.ElementType.eComboMulti, "PERSON_TYPE",
                "Тип участника", "DCT_PERSON_TYPE"));

        element.elements.add(new Element("participant", Element.ElementType.eTextNum, "PERSON_IIN",
                "Иин участника"));

        element.elements.add(new Element("participant", Element.ElementType.eTextNum, "GUILT_PERCENTAGE",
                "% виновности"));

        element.elements.add(new Element("participant", Element.ElementType.eText, "GUILTY_CONTRACT_NO",
                "Номер договора другой страховой компании"));

        element.elements.add(new Element("participant", Element.ElementType.eDate, "GUILTY_CONTRACT_DATE",
                "Дата договора другой страховой компании"));

        element.elements.add(new Element("participant", Element.ElementType.eCombo, "THIRD_PART_INSURER",
                "страховая компания", "DCT_THIRD_PART_INSURER"));
    }

    public static void applyTemplateForObjects(Element element) {

        element.elements.add(new Element("photo", Element.ElementType.ePhoto, "PHOTO", "Фото " + element.description, new String[] {
                "Передняя часть ТС",
                "Задняя часть ТС",
                "Левая передняя сторона 45 град",
                "Правая передняя сторона 45 град",
                "Левая задняя сторона 45 град",
                "Правая задняя сторона 45 град",
                "VIN номер кузова ТС",
                "Гос. номер"
        }));

        element.elements.add(new Element("object", Element.ElementType.ePlan, "DAMAGE_PLAN", "Повреждения"));

        element.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_TYPE",
                "тип объекта",
                "DCT_OBJECT_TYPE"));

        element.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_SUB_TYPE",
                "подтип объекта",
                "DCT_OBJECT_SUB_TYPE"));

        element.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_PRODUCTION",
                "марка",
                "DCT_OBJECT_PRODUCTION"));

        element.elements.add(new Element("object", Element.ElementType.eCombo, "OBJECT_MODEL",
                "модель",
                "DCT_OBJECT_MODEL"));

        element.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_CHASSIS_NO_VIN",
                "№ кузова, № шасси"));

        element.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_REGISRATION_NUMBER",
                "Регистрационный номер объекта"));

        element.elements.add(new Element("object", Element.ElementType.eText, "OBJECT_ENGINE_NO",
                "Номер двигателя"));

        element.elements.add(new Element("object", Element.ElementType.eTextNum, "PRODUCTION_YEAR",
                "Год производства"));

        element.elements.add(new Element("object", Element.ElementType.eTextNum, "PRODUCTION_MONTH",
                "Месяц производства"));

        element.elements.add(new Element("object", Element.ElementType.eText, "CAR_COLOUR",
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

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_CODE",
                            "Код вопроса"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eText, "QUESTION_DESC",
                            "Описание вопроса"));


                    form.elements.add(new Element("", Element.ElementType.eText, "QUESTION_ANSWER_TYPE",
                            "Тип ответа"));

                    form.elements.add(new Element("additionalInfo", Element.ElementType.eCombo, "QUESTION_ANSWER",
                            "Ответ на вопрос", "DCT_QUESTION_ANSWER"));

                    form.elements.add(new Element("description", Element.ElementType.eAnima, "PLAN","План ДТП"));
                    form.elements.add(new Element("description", Element.ElementType.eText, "DESCRIPTION","Описание событий"));

                    form.elements.add(new Element("attachments", Element.ElementType.eCombo, "ATTACHMENT_TYPE",
                            "Тип документа", "DCT_ATTACHMENT_TYPE"));
                    form.elements.add(new Element("attachments", Element.ElementType.eText, "ATTACHMENT_COMMENTS",
                            "Комментарии по документу"));

                    break;
                default:
                    break;
            }
            //now insert values from "input" field, posted by INSIS
            form.applyInput(form.elements);
        }

    }

}
