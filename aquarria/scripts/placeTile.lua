return function(player, item)
  local world = player:getWorld()
  local focus = player:getWorldFocus()
  local tileX = math.floor(focus.x)
  local tileY = math.floor(focus.y)

  if world:getTileType(tileX, tileY) == TileType.air then
    world:setTileType(tileX, tileY, GameRegistry.getTileType(item.placedTile))
    return true
  end

  return false
end
