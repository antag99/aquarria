return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)
  local wallType = GameRegistry:getWallType(
    item:getType():getConfig():getString("placedWall"))

  return world:placeWall(tileX, tileY, wall, player)
end
