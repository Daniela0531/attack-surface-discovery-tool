package org.example;

public class ProjectCopyPreparationErrors extends Exception {

    // Определяем типы ошибок
    public enum ErrorType {
        UNKNOWN_ARCHIVE_TYPE,     // archive type not supported
        WORKING_WITH_FILES_ERROR  // error working with files
    }

    private final ErrorType type;

    public ProjectCopyPreparationErrors(ErrorType type , String message) {
        super(message);
        this.type = type;
    }

    public ProjectCopyPreparationErrors(ErrorType type , String message , Throwable cause) {
        super(message , cause);
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}
