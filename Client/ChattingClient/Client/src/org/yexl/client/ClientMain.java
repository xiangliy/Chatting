// main class for GUI
package org.yexl.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;  
import javafx.event.EventHandler;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;  
   
public class ClientMain extends Application{  
	
    Scene signinScene, messageScene;
    Stage currentStage;
    public static void main(String[] args) {  
        launch(args);  
    }  
    
   @Override  
   public void start(Stage primaryStage) {  
	   
	   currentStage = primaryStage;
       primaryStage.setTitle("Sign In");  
       
       Label nicknameLabel = new Label("Nickname");
       
       TextField receivedMessage = new TextField("");
       
       TextField nicknameText = new TextField();
       
       Button signinBtn = new Button();  
       signinBtn.setText("Sign In");  
       signinBtn.setOnAction(new EventHandler<ActionEvent>() {  
           @Override  
           public void handle(ActionEvent event) {
        	   
        	   if(nicknameText.getText() == null){
        		   return;
        	   }
        	   
        	   //sign in 
        	   if(NicknameManager.getInstance().signin(nicknameText.getText())){
        		   currentStage.setTitle("Send Message");
            	   currentStage.setScene(messageScene);
            	   
                   Thread receiverThread = new Thread(new MessageReceiver(receivedMessage));  
                   receiverThread.start();  
                   
                   Thread KeepAliveThread = new Thread(new KeepAlive());  
                   KeepAliveThread.start(); 
                   
        	   }
           }  
       });  
       
       HBox hbChild = new HBox();
       hbChild.getChildren().addAll(nicknameLabel, nicknameText);
       hbChild.setSpacing(10);
       
       HBox hbParent = new HBox();
       hbParent.getChildren().addAll(hbChild, signinBtn);
       hbParent.setSpacing(10);
         
       StackPane signinPane = new StackPane();  
       signinPane.getChildren().add(hbParent); 
       
       signinScene = new Scene(signinPane, 300, 50);
       
       // message scene
       TextField receiver = new TextField();
       Label receiverlbl = new Label("Receiver:");
       
       TextField message = new TextField();
       Label messagelbl = new Label("Message:");
       
       Button sendBtn = new Button();  
       sendBtn.setText("send");  
       sendBtn.setOnAction(new EventHandler<ActionEvent>() {  
           @Override  
           public void handle(ActionEvent event) {  
        	   //send one message
        	   MessageSender.getInstance().sendOneMessage(receiver.getText(), message.getText());
        	   currentStage.setScene(messageScene);
           }  
       });
       
       Button broadcastBtn = new Button();  
       broadcastBtn.setText("broadcast");  
       broadcastBtn.setOnAction(new EventHandler<ActionEvent>() {  
           @Override  
           public void handle(ActionEvent event) {  
               //broadcast
        	   MessageSender.getInstance().broadcastMessage(message.getText());
        	   currentStage.setScene(messageScene);
           }  
       });
       
       ListView<String> nicknameList = new ListView<String>();
       ObservableList<String> items = FXCollections.observableArrayList();
       items.addAll(NicknameManager.getInstance().getNicknameList());
       nicknameList.setItems(items);
       
       Button refreshBtn = new Button();  
       refreshBtn.setText("refresh");  
       refreshBtn.setOnAction(new EventHandler<ActionEvent>() {  
           @Override  
           public void handle(ActionEvent event) {  
        	   items.clear();
        	   items.addAll(NicknameManager.getInstance().getNicknameList());
               nicknameList.setItems(items);
           }  
       });
       
       HBox receiverBox = new HBox();
       receiverBox.getChildren().addAll(receiverlbl, receiver);
       
       HBox messageBox = new HBox();
       messageBox.getChildren().addAll(messagelbl, message);
       
       HBox buttons = new HBox();;
       buttons.getChildren().addAll(sendBtn, broadcastBtn);
       buttons.setSpacing(10);
       
       VBox sendMessageBox = new VBox();
       sendMessageBox.getChildren().addAll(receiverBox, messageBox, buttons, receivedMessage);
       sendMessageBox.setSpacing(10);
       
       HBox all = new HBox();
       all.getChildren().addAll(sendMessageBox, nicknameList);
       all.setSpacing(10);
       
       StackPane messagePane = new StackPane();  
       messagePane.getChildren().add(all); 
       
       
       messageScene = new Scene(messagePane, 500, 300);
       
       primaryStage.setScene(signinScene);  
       primaryStage.show();  
    }  
}  