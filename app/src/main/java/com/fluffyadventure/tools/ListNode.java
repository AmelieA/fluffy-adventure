package com.fluffyadventure.tools;

import com.fluffyadventure.model.Creature;

/**
 * Created by Johan on 04/03/2015.
 */
class ListNode {
    public ListNode next;
    public Creature fighter;

    public ListNode(Creature fighter, ListNode next) {
        this.next = next;
        this.fighter = fighter;
    }
}
