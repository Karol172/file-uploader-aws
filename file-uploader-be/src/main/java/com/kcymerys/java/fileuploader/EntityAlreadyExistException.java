package com.kcymerys.java.fileuploader;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(String msg) {
        super(msg);
    }
}
