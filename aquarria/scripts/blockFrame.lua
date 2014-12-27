local generalFrame = require "generalFrame"

local function blockFrame(world, x, y)
  local mergeTop = not world:inBounds(x, y + 1) or
    world:getTileType(x, y + 1):isSolid()
  local mergeRight = not world:inBounds(x + 1, y) or
    world:getTileType(x + 1, y):isSolid()
  local mergeBottom = not world:inBounds(x, y - 1) or
    world:getTileType(x, y - 1):isSolid()
  local mergeLeft = not world:inBounds(x - 1, y) or
    world:getTileType(x - 1, y):isSolid()
  return generalFrame(mergeTop, mergeRight, mergeBottom, mergeLeft)
end

return blockFrame
