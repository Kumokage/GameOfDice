package hsealexglushko;

import java.util.Random;

public class Player extends Thread implements Comparable<Player>{

    private int countOfWins;
    private int numberOfDice;
    final private static Random random = new Random();
    final private Commentator commentator;
    final private static int AMOUNT_OF_SIDES = 6;

    public Player(String name, Commentator commentator, int numberOfDice){
        super(name);
        this.commentator = commentator;
        countOfWins = 0;
        this.numberOfDice = numberOfDice;
    }

    public int getCountOfWins(){
        return countOfWins;
    }

    public void winIncrease(){
        ++countOfWins;
    }

    int getPoints(){
        int points = 0;
        for(int i = 0; i < numberOfDice; i++){
            points += random.nextInt(AMOUNT_OF_SIDES) + 1;
        }
        return points;
    }

    @Override
    public void run(){
        synchronized(commentator){
            try{
                while(commentator.getNumberOfRoundsLeft() != 1){

                    if(commentator.getHowManyPlayersLast() > 1){
                        if(commentator.getCurrentMaxScore() != AMOUNT_OF_SIDES * numberOfDice){
                            commentator.writeResult(getPoints(), this);
                        } else {
                            commentator.decreaseNumberOfPlayers();
                        }
                        commentator.wait();
                    } else{
                        if(commentator.getCurrentMaxScore() != AMOUNT_OF_SIDES * numberOfDice){
                            commentator.writeResult(getPoints(), this);
                        } else {
                            commentator.decreaseNumberOfPlayers();
                            commentator.printRoundWinner();
                        }
                        commentator.notifyAll();
                    }
                }
                if(commentator.getHowManyPlayersLast() > 1){
                    if(commentator.getCurrentMaxScore() != AMOUNT_OF_SIDES * numberOfDice){
                        commentator.writeResult(getPoints(), this);
                    } else {
                        commentator.decreaseNumberOfPlayers();
                    }
                } else{
                    if(commentator.getCurrentMaxScore() != AMOUNT_OF_SIDES * numberOfDice){
                        commentator.writeResult(getPoints(), this);
                    } else {
                        commentator.decreaseNumberOfPlayers();
                        commentator.printMatchWinner();
                    }
                }

            } catch(InterruptedException e){
                System.out.println("Something go wrong! Thread name: " + getName());
            }  catch(Exception e){
                System.out.println("Something go wrong " + e);
            }
        }
    }

    @Override
    public int compareTo(Player o){
        return o.countOfWins - countOfWins;
    }

    @Override
    public String toString(){
        return getName();
    }
}
