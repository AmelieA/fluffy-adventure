package com.fluffyadventure.tools;

import com.fluffyadventure.model.Creature;

/**
 * Created by Johan on 04/03/2015.
 */
public class CircularLinkedList {
    private ListNode head = null;
    private int numberOfFighters = 0;
    private ListNode actualFighter = null;
    private int index = 0;

    public boolean isEmpty() {
        return (numberOfFighters == 0);
    }

    public int getNumberOfElements() {
        return numberOfFighters;
    }

    public void insertFirst(Creature fighter) {
        if (!(isEmpty())) {
            index++;
        }
        ListNode listNode = new ListNode(fighter, head);
        head = listNode;
        numberOfFighters++;
    }

    public void insertAfterActual(Creature fighter) {
        ListNode listNode = new ListNode(fighter, actualFighter.next);
        actualFighter.next = listNode;
        numberOfFighters++;
    }

    public boolean deleteFirst() {
        if (isEmpty())
            return false;
        if (index > 0)
            index--;
        head = head.next;
        numberOfFighters--;
        return true;
    }

    public boolean deleteActualFighter() {
        if (index > 0) {
            numberOfFighters--;
            index--;
            ListNode listNode = head;
            while (listNode.next.equals(actualFighter) == false)
                listNode = listNode.next;
            listNode.next = actualFighter.next;
            actualFighter = listNode;
            return true;
        }
        else {
            actualFighter = head.next;
            index = 0;
            return deleteFirst();
        }
    }

    public boolean goToNextFighter() {
        if (isEmpty())
            return false;
        index = (index + 1) % numberOfFighters;
        if (index == 0)
            actualFighter = head;
        else
            actualFighter = actualFighter.next;
        return true;
    }

    public Creature  getActualFighter() {
        return actualFighter.fighter;
    }

    public void setActualFighter(Creature fighter) {
        actualFighter.fighter = fighter;
    }
}
