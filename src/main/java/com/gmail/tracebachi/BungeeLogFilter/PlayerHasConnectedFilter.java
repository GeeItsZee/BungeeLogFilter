/*
 * BungeeLogFilter - Filtering support for BungeeCord logging
 * Copyright (C) 2017 tracebachi@gmail.com (GeeItsZee)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.tracebachi.BungeeLogFilter;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
public class PlayerHasConnectedFilter implements Filter
{
  private volatile boolean filterPing;
  private volatile boolean filterJoin;

  @Override
  public boolean isLoggable(LogRecord logRecord)
  {
    // Nothing to do if both filters are disabled
    if (!filterJoin && !filterJoin)
    {
      return true;
    }

    // Pings and joins have an INFO level
    if (logRecord.getLevel() != Level.INFO)
    {
      return true;
    }

    String message = logRecord.getMessage();

    // Pings and joins have "{0} has connected" as the message
    if (!"{0} has connected".equals(message))
    {
      return true;
    }

    // Pings and joins have exactly 1 parameter
    if (logRecord.getParameters() == null || logRecord.getParameters().length != 1)
    {
      return true;
    }

    String paramAsString = (logRecord.getParameters()[0]).toString();

    // The parameter for pings and joins ends with this string
    if (!paramAsString.endsWith("] <-> InitialHandler"))
    {
      return true;
    }

    // Pings are formatted like "[/192.16.0.1]"
    if (filterPing && paramAsString.startsWith("[/"))
    {
      return false;
    }

    // Joins are formatted like "[Notch]"
    if (filterJoin && !paramAsString.startsWith("[/") && paramAsString.startsWith("["))
    {
      return false;
    }

    return true;
  }

  public boolean shouldFilterPing()
  {
    return filterPing;
  }

  public void setFilterPing(boolean filterPing)
  {
    this.filterPing = filterPing;
  }

  public boolean shouldFilterJoin()
  {
    return filterJoin;
  }

  public void setFilterJoin(boolean filterJoin)
  {
    this.filterJoin = filterJoin;
  }
}
