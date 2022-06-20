package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "messagebox")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Message {

    String id;
    String dataName;
    String messageType;
    String dataUploadTime;
    String dataExamineTime;
    String dataCache;
    String messageRequest;
    String reply;
    String fileId;
    String messageSender;
    String messageReceiver;
    String messageResponse;
    String replyUser;


    }

