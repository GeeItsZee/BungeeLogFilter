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

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * @author Trace Bachi (tracebachi@gmail.com) (GeeItsZee)
 */
public class BungeeLogFilterPlugin extends Plugin
{
  private Filter originalFilter;
  private CompoundFilter compoundFilter;
  private PlayerHasConnectedFilter playerHasConnectedFilter;
  private RegexPatternsFilter regexPatternsFilter;
  private BLFReloadCommand reloadCommand;

  @Override
  public void onEnable()
  {
    // Save a reference to the original filter
    originalFilter = getProxy().getLogger().getFilter();

    Configuration configuration;

    try
    {
      // Try to load the configuration file
      configuration = reloadConfiguration();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return;
    }

    // Create our own filters
    playerHasConnectedFilter = new PlayerHasConnectedFilter();
    regexPatternsFilter = new RegexPatternsFilter();

    // Read the configuration (and set up the custom filters)
    readConfiguration(configuration);

    // Build a list of filters and give the original filter the highest priority
    List<Filter> newFilters = new ArrayList<>();
    newFilters.add(originalFilter);
    newFilters.add(playerHasConnectedFilter);
    newFilters.add(regexPatternsFilter);

    // Build a CompoundFilter with the new filters
    compoundFilter = new CompoundFilter(newFilters);

    // Set the filter for the proxy
    getProxy().getLogger().setFilter(compoundFilter);

    // Add the reload command
    reloadCommand = new BLFReloadCommand(this);
    getProxy().getPluginManager().registerCommand(this, reloadCommand);
  }

  @Override
  public void onDisable()
  {
    // Unregister the command
    if (reloadCommand != null)
    {
      getProxy().getPluginManager().unregisterCommand(reloadCommand);
      reloadCommand = null;
    }

    // Replace the filter with the original
    getProxy().getLogger().setFilter(originalFilter);

    // Clear the filters stored in CompoundFilter
    if (compoundFilter != null)
    {
      compoundFilter.clearFilters();
      compoundFilter = null;
    }

    // Clean up our filters
    regexPatternsFilter = null;
    playerHasConnectedFilter = null;

    // Remove the reference to the original filter
    originalFilter = null;
  }

  Configuration reloadConfiguration() throws IOException
  {
    File file = ConfigUtil.saveResource(this, "config.yml", "config.yml", false);
    return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
  }

  void readConfiguration(Configuration configuration)
  {
    boolean filterPing = configuration.getBoolean("FilterPing", true);
    boolean filterJoin = configuration.getBoolean("FilterJoin", true);

    // Update the filter
    playerHasConnectedFilter.setFilterPing(filterPing);
    playerHasConnectedFilter.setFilterJoin(filterJoin);

    // Log the current settings
    getLogger().log(Level.INFO, "FilterPing: {0}", playerHasConnectedFilter.shouldFilterPing());
    getLogger().log(Level.INFO, "FilterJoin: {0}", playerHasConnectedFilter.shouldFilterJoin());

    List<String> filterList = configuration.getStringList("RegexFilters");
    List<Pattern> filterPatternList = new ArrayList<>(filterList.size());

    for (String filter : filterList)
    {
      Pattern compiledPattern = Pattern.compile(filter);
      filterPatternList.add(compiledPattern);

      getLogger().log(Level.INFO, "Using RegEx filter: {0}", compiledPattern.toString());
    }

    // Update the filter
    regexPatternsFilter.setPatternList(filterPatternList);
  }
}
