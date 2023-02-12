package de.uniks.pmws2223.uno.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Card
{
   public static final String PROPERTY_VALUE = "value";
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_OWNER = "owner";
   private int value;
   private String color;
   private Player owner;
   protected PropertyChangeSupport listeners;

   public int getValue()
   {
      return this.value;
   }

   public Card setValue(int value)
   {
      if (value == this.value)
      {
         return this;
      }

      final int oldValue = this.value;
      this.value = value;
      this.firePropertyChange(PROPERTY_VALUE, oldValue, value);
      return this;
   }

   public String getColor()
   {
      return this.color;
   }

   public Card setColor(String value)
   {
      if (Objects.equals(value, this.color))
      {
         return this;
      }

      final String oldValue = this.color;
      this.color = value;
      this.firePropertyChange(PROPERTY_COLOR, oldValue, value);
      return this;
   }

   public Player getOwner()
   {
      return this.owner;
   }

   public Card setOwner(Player value)
   {
      if (this.owner == value)
      {
         return this;
      }

      final Player oldValue = this.owner;
      if (this.owner != null)
      {
         this.owner = null;
         oldValue.withoutCards(this);
      }
      this.owner = value;
      if (value != null)
      {
         value.withCards(this);
      }
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getColor());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setOwner(null);
   }
}
