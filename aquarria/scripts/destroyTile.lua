return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)

  print("destroying tile at " .. tileX .. "," .. tileY)
  return world:destroyTile(tileX, tileY)
end
