package com.maddyhome.idea.vim.option;

/*
* IdeaVim - A Vim emulator plugin for IntelliJ Idea
* Copyright (C) 2003 Rick Maddy
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.List;

/**
 * This is an option that accepts an arbitrary list of values
 */
public class ListOption extends TextOption
{
    /**
     * Creates the option
     * @param name The name of the option
     * @param abbrev The short name
     * @param dflt The option's default values
     * @param pattern A regular expression that is used to validate new values. null if no check needed
     */
    ListOption(String name, String abbrev, String[] dflt, String pattern)
    {
        super(name, abbrev);

        this.dflt = new ArrayList(Arrays.asList(dflt));
        this.value = new ArrayList(this.dflt);
        this.pattern = pattern;
    }

    /**
     * Gets the value of the option as a comma separated list of values
     * @return The option's value
     */
    public String getValue()
    {
        StringBuffer res = new StringBuffer();
        int cnt = 0;
        for (Iterator iterator = value.iterator(); iterator.hasNext(); cnt++)
        {
            String s = (String)iterator.next();
            if (cnt > 0)
            {
                res.append(",");
            }
            res.append(s);
        }

        return res.toString();
    }

    /**
     * Gets the option's values as a list
     * @return The option's values
     */
    public List values()
    {
        return value;
    }

    /**
     * Determines if this option has all the values listed on the supplied value
     * @param val A comma separated list of values to look for
     * @return True if all the supplied values are set in this option, false if not
     */
    public boolean contains(String val)
    {
        return value.containsAll(parseVals(val));
    }

    /**
     * Sets this option to contain just the supplied values. If any of the supplied values are invalid then this
     * option is not updated at all.
     * @param val A comma separated list of values
     * @return True if all the supplied values were correct, false if not
     */
    public boolean set(String val)
    {
        return set(parseVals(val));
    }

    /**
     * Adds the supplied values to the current list of values. If any of the supplied values are invalid then this
     * option is not updated at all.
     * @param val A comma separated list of values
     * @return True if all the supplied values were correct, false if not
     */
    public boolean append(String val)
    {
        return append(parseVals(val));
    }

    /**
     * Adds the supplied values to the start of the current list of values. If any of the supplied values are invalid
     * then this option is not updated at all.
     * @param val A comma separated list of values
     * @return True if all the supplied values were correct, false if not
     */
    public boolean prepend(String val)
    {
        return prepend(parseVals(val));
    }

    /**
     * Removes the supplied values from the current list of values. If any of the supplied values are invalid then this
     * option is not updated at all.
     * @param val A comma separated list of values
     * @return True if all the supplied values were correct, false if not
     */
    public boolean remove(String val)
    {
        return remove(parseVals(val));
    }

    protected boolean set(ArrayList vals)
    {
        if (vals == null)
        {
            return false;
        }

        value = vals;
        fireOptionChangeEvent();

        return true;
    }

    protected boolean append(ArrayList vals)
    {
        if (vals == null)
        {
            return false;
        }

        value.addAll(vals);
        fireOptionChangeEvent();

        return true;
    }

    protected boolean prepend(ArrayList vals)
    {
        if (vals == null)
        {
            return false;
        }

        value.addAll(0, vals);
        fireOptionChangeEvent();

        return true;
    }

    protected boolean remove(ArrayList vals)
    {
        if (vals == null)
        {
            return false;
        }

        value.removeAll(vals);
        fireOptionChangeEvent();

        return true;
    }

    protected ArrayList parseVals(String val)
    {
        ArrayList res = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(val, ",");
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken().trim();
            if (pattern == null || token.matches(pattern))
            {
                res.add(token);
            }
            else
            {
                return null;
            }
        }

        return res;
    }

    /**
     * Checks to see if the current value of the option matches the default value
     * @return True if equal to default, false if not
     */
    public boolean isDefault()
    {
        return dflt.equals(value);
    }

    /**
     * Resets the option to its default value
     */
    public void resetDefault()
    {
        if (!dflt.equals(value))
        {
            value = new ArrayList(dflt);
            fireOptionChangeEvent();
        }
    }

    /**
     * Gets the string representation appropriate for output to :set all
     * @return The option as a string {name}={value list}
     */
    public String toString()
    {
        StringBuffer res = new StringBuffer();
        res.append("  ");
        res.append(getName());
        res.append("=");
        res.append(getValue());

        return res.toString();
    }

    protected ArrayList dflt;
    protected ArrayList value;
    protected String pattern;
}