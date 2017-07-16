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

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
class ConfigUtil
{
  static File saveResource(
    Plugin plugin, String resourceName, String destinationName, boolean replaceIfDestExists)
  {
    File folder = plugin.getDataFolder();

    if (!folder.exists() && !folder.mkdir())
    {
      return null;
    }

    File destinationFile = new File(folder, destinationName);

    try
    {
      if (!destinationFile.exists() || replaceIfDestExists)
      {
        if (destinationFile.createNewFile())
        {
          try (InputStream in = plugin.getResourceAsStream(resourceName);
            OutputStream out = new FileOutputStream(destinationFile))
          {
            ByteStreams.copy(in, out);
          }
        }
        else
        {
          return null;
        }
      }

      return destinationFile;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
