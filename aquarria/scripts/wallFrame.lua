local generalFrame = require "generalFrame"

local function wallFrame(world, x, y)
  local mergeTop = not world:inBounds(x, y + 1) or
    world:getWallType(x, y + 1) ~= WallType.air
  local mergeRight = not world:inBounds(x + 1, y) or
    world:getWallType(x + 1, y) ~= WallType.air
  local mergeBottom = not world:inBounds(x, y - 1) or
    world:getWallType(x, y - 1) ~= WallType.air
  local mergeLeft = not world:inBounds(x - 1, y) or
    world:getWallType(x - 1, y) ~= WallType.air
  return generalFrame(mergeTop, mergeRight, mergeBottom, mergeLeft)
end

return wallFrame
