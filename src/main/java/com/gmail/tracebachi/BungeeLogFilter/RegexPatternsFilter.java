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

import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
public class RegexPatternsFilter implements Filter
{
  private volatile List<Pattern> patternList;

  @Override
  public boolean isLoggable(LogRecord logRecord)
  {
    // Nothing to do if there are no patterns
    if (patternList == null || patternList.isEmpty())
    {
      return true;
    }

    String message = logRecord.getMessage();

    for (Pattern pattern : patternList)
    {
      if (pattern == null)
      {
        continue;
      }

      // If the message matches the pattern, the record should be filtered out.
      if (pattern.matcher(message).matches())
      {
        return false;
      }
    }

    return true;
  }

  public List<Pattern> getPatternList()
  {
    return patternList;
  }

  public void setPatternList(List<Pattern> patternList)
  {
    this.patternList = patternList;
  }
}
