return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)

  if world:getWallType(tileX, tileY) == WallType.air then
    world:setWallType(tileX, tileY, GameRegistry.getWallType(item.placedWall))
    return true
  end

  return false
end
