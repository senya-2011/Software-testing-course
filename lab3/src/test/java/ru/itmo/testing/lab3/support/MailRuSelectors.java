package ru.itmo.testing.lab3.support;

public final class MailRuSelectors {

    public static final String LOGIN_URL = "https://account.mail.ru/login";
    public static final String COMPOSE_URL = "https://e.mail.ru/compose/";
    public static final String PAGE_BODY = "/html/body";

    public static final String LOGIN_PAGE = "//*[@data-page='MAILRU_LOGIN_SCREEN']";
    public static final String LOGIN_INPUT = LOGIN_PAGE + "//*[@placeholder='Email account name']";
    public static final String LOGIN_CONTINUE_BUTTON = LOGIN_PAGE + "//*[@data-test-id='continue-button']";

    public static final String MAIL_APP_WRITE_BUTTON = "//a[contains(@href, '/compose/') and contains(normalize-space(.), 'Написать письмо')]";
    public static final String SEARCH_OPEN_BUTTON =
        "//*[contains(@class, 'search-panel-button') and contains(., 'Поиск по почте')][1]";
    public static final String SEARCH_INPUT = "//input[contains(@class, 'mail-operands_dynamic-input__input')]";
    public static final String COMPOSE_RECIPIENTS_CONTAINER = "//*[@data-name='to']";
    public static final String COMPOSE_TO_INPUT = "//*[@data-name='to']//input[@type='text']";
    public static final String COMPOSE_SUBJECT_INPUT = "//input[@name='Subject']";
    public static final String COMPOSE_BODY_EDITOR =
        "//*[contains(@class, 'cke_editable_inline') and @role='textbox' and @aria-multiline='true'"
            + " and @contenteditable='true' and not(@data-cke-widget-editable)]";
    public static final String COMPOSE_SEND_BUTTON = "//*[@data-test-id='send']";
    public static final String COMPOSE_SAVE_BUTTON = "//*[@data-test-id='save'] | //button[normalize-space()='Сохранить'] | //*[@title='Сохранить']";
    public static final String COMPOSE_EDITOR_READY_MARKERS =
        "(" + COMPOSE_BODY_EDITOR + " | " + COMPOSE_SAVE_BUTTON + " | " + COMPOSE_SEND_BUTTON + ")";
    public static final String SEND_ACCEPTANCE_MARKERS =
        "("
            + "//*[contains(normalize-space(.), 'Отправлено') and contains(normalize-space(.), 'можно возвращать')]"
            + " | "
            + "//*[normalize-space()='Сохранить в шаблон']"
            + " | "
            + "//*[normalize-space()='Поделиться письмом']"
            + ")";

    public static final String MESSAGE_DELETE_BUTTON = "//*[@title='Удалить' and contains(@class, 'button2_delete')]";
    public static final String MESSAGE_MOVE_TO_FOLDER_BUTTON = "//*[@title='В папку' and contains(@class, 'button2')]";
    public static final String MESSAGE_MORE_BUTTON = "//*[@title='Ещё' and contains(@class, 'button2')]";
    public static final String MESSAGE_DELETE_FOREVER_BUTTON = "//*[self::button or self::span][normalize-space()='Удалить навсегда']";
    public static final String MESSAGE_VIEW_ACTIONS =
        "(" + MESSAGE_DELETE_BUTTON
            + " | " + MESSAGE_MOVE_TO_FOLDER_BUTTON
            + " | " + MESSAGE_MORE_BUTTON
            + " | " + MESSAGE_DELETE_FOREVER_BUTTON
            + ")";
    public static final String MESSAGE_VIEW_TEXT_ROOT =
        "//*[@id='app-canvas']//*[contains(@class, 'b-letter')"
            + " or contains(@class, 'thread')"
            + " or contains(@class, 'message')"
            + " or contains(@class, 'letter__body')"
            + " or contains(@class, 'letter-body')]";
    public static final String MESSAGE_VIEW_FALLBACK_ROOT =
        "//*[@id='app-canvas']//*[contains(@class, 'layout__overflow-container')]";
    public static final String NEW_FOLDER_BUTTON = "//*[@data-qa-id='new-folder-btn']";
    public static final String NEW_FOLDER_NAME_INPUT = "//input[@name='name']";
    public static final String NEW_FOLDER_SUBMIT_BUTTON = "//button[normalize-space()='Добавить папку']";
    public static final String DELETE_FOLDER_CONFIRM_BUTTON =
        "//*[contains(@class, 'layer-confirm-delete-folder')]//*[contains(@class, 'layer__submit-button') and normalize-space()='Удалить']";

    public static final String ACCEPT_ALL_BUTTON = "//*[self::a or self::button][normalize-space()='Accept all']";
    public static final String DISMISS_HELPER_BUTTON = "//*[@role='button' and normalize-space()='Хорошо']";

    private MailRuSelectors() {
    }
}
