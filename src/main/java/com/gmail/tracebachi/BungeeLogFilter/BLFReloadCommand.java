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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
public class BLFReloadCommand extends Command
{
  private static final String COMMAND_PERM = "BungeeLogFilter.Reload";

  private final BungeeLogFilterPlugin plugin;

  public BLFReloadCommand(BungeeLogFilterPlugin plugin)
  {
    super("BLFreload", COMMAND_PERM, "bungeelogfilterreload");
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSender commandSender, String[] strings)
  {
    if (!commandSender.hasPermission(COMMAND_PERM))
    {
      return;
    }

    try
    {
      Configuration configuration = plugin.reloadConfiguration();
      plugin.readConfiguration(configuration);

      commandSender.sendMessage(ChatColor.GREEN + "[BungeeLogFilter] Configuration reloaded");
    }
    catch (IOException e)
    {
      e.printStackTrace();
      commandSender.sendMessage(ChatColor.RED + "[BungeeLogFilter] Exception when reading configuration");
    }
  }
}
