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
//    String email;
//    String id;
//    String dataUploaderId;
//    String dataDownloaderId;
//    String dataExaminer;
//    String datatype;
//    String dataUploadTime;
//    String dataDownloadTime;
//    String dataExamineTime;
//    String messageType;
//    String roles;
//    String dataName;
//    String[] tags;


    String dataName;
    String messageType;
    String dataUploadTime;
    String dataExamineTime;
    String dataCache;
    String messageRequest;
    String reply;
    String messageId;
    String messageSender;
    String messageReceiver;
    String messageResponse;
    String id;

    }

