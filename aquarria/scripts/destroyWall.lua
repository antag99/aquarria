return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)

  return world:destroyWall(tileX, tileY)
end
