import java.util.*;

public class AK47{
   public static void main(String[] args){
      Scanner input=new Scanner(System.in);
      System.out.println("Enter number of players(2-6):");
      int num=input.nextInt();
      
      // create lists of decks and fill them
      ArrayList<Deck> players=new ArrayList<>(), extra=new ArrayList<>();
      for(int i=0;i<num;i++){
         players.add(new Deck());
         extra.add(new Deck());
      }
      
      //create the playing deck and the deck for the cards on the floor
      Deck playingDeck=new Deck();
      Deck recycleDeck=new Deck();
      //populate the playing deck and shuffle it
      playingDeck.createPlayingDeck();
      playingDeck.shuffleDeck();
      
      //get user player number
      System.out.println("Choose player number[1-"+num+"]:");
      int player=input.nextInt();
      
      //dish out cards to the players
      Deck tempDeck;
      for(int j=0;j<4;j++){
         for(int i=0;i<num;i++){
            tempDeck=players.get(i);
            tempDeck.addCard(playingDeck.drawCard());
            players.set(i,tempDeck);
         }
      }
      
      //NPC splits the cards[A,K,4,7] from the rest
      recycleDeck.addCard(playingDeck.drawCard());
      int x=1;
      for(Deck deck: players){
         Deck newDeck=new Deck();
         if(player!=x){
            ArrayList<Card> d=deck.getDeck();
            ArrayList<Value> vals=new ArrayList<>();
            for(Card card: d){
               Value val=card.getValue();
               if((val==Value.ACE || val==Value.KING || val==Value.FOUR || val==Value.SEVEN) && !vals.contains(val)){
                  tempDeck=extra.get(x-1);
                  tempDeck.addCard(card);
                  extra.set(x-1,tempDeck);
               }
               else{newDeck.addCard(card);}
            }
         }
         else{
            newDeck=players.get(x-1);
         }
         players.set(x-1,newDeck);
         x++;
      }
      
      boolean endgame=false;
      Card tempCard;
      Value tempVal;
      while(!endgame){
         //if playing deck is empty, clean the cards on the floor and add the cards to the playing deck
         if(playingDeck.size()==0){
            tempCard=recycleDeck.drawCard();
            playingDeck=recycleDeck;
            recycleDeck.emptyDeck();
            recycleDeck.addCard(tempCard);
            playingDeck.shuffleDeck();
         }
         
         //each player gets a turn to play
         for(int i=0;i<num;i++){
            if(i+1!=player){
               int n=i+1;
               System.out.println("Player "+n+":");
               tempCard=recycleDeck.peek();//look at the last card dropped
               tempVal=tempCard.getValue();
               //if the card that was last dropped is a [A, K, 4, or 7]
               if((tempVal==Value.ACE || tempVal==Value.KING || tempVal==Value.FOUR || tempVal==Value.SEVEN) && !extra.get(i).contains(tempVal)){
                  System.out.print("Takes from the ground.");
                  tempDeck=extra.get(i);
                  tempDeck.addCard(recycleDeck.drawCard());
                  extra.set(i,tempDeck);
                  tempDeck=players.get(i);
                  tempCard=tempDeck.drawCard();
                  players.set(i,tempDeck);
                  System.out.println("Then drops a card");
                  recycleDeck.addCard(tempCard);
               }
               else{
                  tempCard=playingDeck.drawCard();
                  tempVal=tempCard.getValue();
                  System.out.println("Draws from the deck.");
                  //if the card that was drawn is a [A, K, 4, or 7]
                  if((tempVal==Value.ACE || tempVal==Value.KING || tempVal==Value.FOUR || tempVal==Value.SEVEN) && !extra.get(i).contains(tempVal)){
                     tempDeck=extra.get(i);
                     tempDeck.addCard(tempCard);
                     extra.set(i,tempDeck);
                     tempDeck=players.get(i);
                     tempCard=tempDeck.drawCard();
                     players.set(i,tempDeck);
                     System.out.println("Then drops a card");
                     recycleDeck.addCard(tempCard);
                  }
                  else{
                     System.out.println("Then drops a card");
                     recycleDeck.addCard(tempCard);
                  }
               }
               System.out.println("");
               //Check if the player has won
               if(extra.get(i).win()){
                  System.out.println("Player "+n+" wins!");
                  System.out.println("Their cards:");
                  System.out.print(extra.get(i).toString());
                  endgame=true;
                  break;
               }
            }
            //Player input: When its the player's turn
            else{
               tempCard=recycleDeck.peek();
               System.out.println("Your turn:");
               System.out.println("Card on the floor: "+tempCard.toString());
               System.out.println("Your cards:");
               System.out.print(players.get(i).toString());
               System.out.println("Do you want to draw from [1]deck or [2]floor?");
               int ans=input.nextInt();
               
               if(ans==1){//if the user chooses to draw from the deck
                  tempCard=playingDeck.drawCard();
                  System.out.println("You draw a "+tempCard.toString());
                  System.out.println("Do you want to [1]keep or [2]throw away the card?");
                  ans=input.nextInt();
                  if(ans==1){
                     System.out.println("Throw away a card");
                     ans=input.nextInt();
                     tempDeck=players.get(i);
                     tempDeck.addCard(tempCard);
                     players.set(i,tempDeck);
                     tempCard=players.get(i).getCard(ans);
                     System.out.println("You drop card: "+ tempCard.toString());//drop one of the cards in hand
                     recycleDeck.addCard(tempCard);
                  }
                  else if(ans==2){
                     System.out.println("You throw away the card");
                     recycleDeck.addCard(tempCard);
                  }
               }
               else if(ans==2){//if the user chooses to take the card on the floor
                  System.out.println("Throw away a card");
                  ans=input.nextInt();
                  Card temp=recycleDeck.drawCard();
                  tempCard=players.get(i).getCard(ans);
                  System.out.println("You drop card: "+ tempCard.toString());//drop one of the cards in hand
                  recycleDeck.addCard(tempCard);
                  tempDeck=players.get(i);
                  tempDeck.addCard(temp);
                  players.set(i,tempDeck);
               }
               System.out.println("");
               
               if(players.get(i).win()){//Check if the player has won
                  System.out.println("You win!");
                  System.out.println("Your cards:");
                  System.out.print(players.get(i).toString());
                  endgame=true;
                  break;
               }
               
            }
            
         }
      }
      
   }
}
