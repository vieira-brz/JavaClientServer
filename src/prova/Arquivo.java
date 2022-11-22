package prova;

import java.io.*;

public class Arquivo implements Serializable {

   private static final long serialVersionUID = 1L;

   private String name;
   private byte[] content;
   private transient long sizeKB;
   
   
   // Buscando nome
   public String getName() {
	   return name;
   }
   
   
   // Setando nome
   public void setName(String name) {
       this.name = name;
   }

   
   // Buscando content
   public byte[] getContent() {
       return content;
   }

   
   // Setando content
   public void setContent(byte[] content) {
       this.content = content;
   }

   
   // Buscando KBites
   public long getSizeKB() {
       return sizeKB;
   }

   
   // Setando KBites
   public void setSizeKB(long sizeKB) {
       this.sizeKB = sizeKB;
   }

}