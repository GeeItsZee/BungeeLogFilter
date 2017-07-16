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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
public class CompoundFilter implements Filter
{
  private final List<Filter> filterList;

  public CompoundFilter(List<Filter> filterList)
  {
    this.filterList = new CopyOnWriteArrayList<>(filterList);
  }

  public boolean addFilters(Collection<Filter> filters)
  {
    return filterList.addAll(filters);
  }

  public boolean removeFilters(Collection<Filter> filters)
  {
    return filterList.removeAll(filters);
  }

  public void clearFilters()
  {
    filterList.clear();
  }

  @Override
  public boolean isLoggable(LogRecord logRecord)
  {
    for (Filter filter : filterList)
    {
      // Ignore null filters if they exist
      if (filter == null)
      {
        continue;
      }

      try
      {
        if (!filter.isLoggable(logRecord))
        {
          return false;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    return true;
  }
}
