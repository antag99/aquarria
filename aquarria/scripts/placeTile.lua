return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)
  local tileType = GameRegistry:getTileType(
    item:getType():getConfig():getString("placedTile"))

  return world:placeTile(tileX, tileY, tileType, player)
end
