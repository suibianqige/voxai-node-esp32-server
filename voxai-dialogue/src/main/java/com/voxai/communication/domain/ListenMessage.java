package com.voxai.communication.domain;

import com.voxai.enums.ListenMode;
import com.voxai.enums.ListenState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class ListenMessage extends Message {
    public ListenMessage(){
        super("listen");
    }

    private ListenState state;
    private ListenMode mode;
    private String text;
}
