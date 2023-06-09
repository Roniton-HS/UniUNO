package de.uniks.pmws2223.uno.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Game
{
   public static final String PROPERTY_CLOCKWISE = "clockwise";
   public static final String PROPERTY_PLAYERS = "players";
   public static final String PROPERTY_CURRENT_PLAYER = "currentPlayer";
   public static final String PROPERTY_CURRENT_CARD = "currentCard";
   protected PropertyChangeSupport listeners;
   private boolean clockwise;
   private List<Player> players;
   private Player currentPlayer;
   private Card currentCard;

   public boolean isClockwise()
   {
      return this.clockwise;
   }

   public Game setClockwise(boolean value)
   {
      if (value == this.clockwise)
      {
         return this;
      }

      final boolean oldValue = this.clockwise;
      this.clockwise = value;
      this.firePropertyChange(PROPERTY_CLOCKWISE, oldValue, value);
      return this;
   }

   public List<Player> getPlayers()
   {
      return this.players != null ? Collections.unmodifiableList(this.players) : Collections.emptyList();
   }

   public Game withPlayers(Player value)
   {
      if (this.players == null)
      {
         this.players = new ArrayList<>();
      }
      if (this.players.add(value))
      {
         this.firePropertyChange(PROPERTY_PLAYERS, null, value);
      }
      return this;
   }

   public Game withPlayers(Player... value)
   {
      for (final Player item : value)
      {
         this.withPlayers(item);
      }
      return this;
   }

   public Game withPlayers(Collection<? extends Player> value)
   {
      for (final Player item : value)
      {
         this.withPlayers(item);
      }
      return this;
   }

   public Game withoutPlayers(Player value)
   {
      if (this.players != null && this.players.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_PLAYERS, value, null);
      }
      return this;
   }

   public Game withoutPlayers(Player... value)
   {
      for (final Player item : value)
      {
         this.withoutPlayers(item);
      }
      return this;
   }

   public Game withoutPlayers(Collection<? extends Player> value)
   {
      for (final Player item : value)
      {
         this.withoutPlayers(item);
      }
      return this;
   }

   public Player getCurrentPlayer()
   {
      return this.currentPlayer;
   }

   public Game setCurrentPlayer(Player value)
   {
      if (this.currentPlayer == value)
      {
         return this;
      }

      final Player oldValue = this.currentPlayer;
      if (this.currentPlayer != null)
      {
         this.currentPlayer = null;
         oldValue.setGame(null);
      }
      this.currentPlayer = value;
      if (value != null)
      {
         value.setGame(this);
      }
      this.firePropertyChange(PROPERTY_CURRENT_PLAYER, oldValue, value);
      return this;
   }

   public Card getCurrentCard()
   {
      return this.currentCard;
   }

   public Game setCurrentCard(Card value)
   {
      if (this.currentCard == value)
      {
         return this;
      }

      final Card oldValue = this.currentCard;
      this.currentCard = value;
      this.firePropertyChange(PROPERTY_CURRENT_CARD, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   public void removeYou()
   {
      this.setCurrentPlayer(null);
      this.setCurrentCard(null);
   }
}
