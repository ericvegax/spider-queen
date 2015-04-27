package sqr.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

// Referenced classes of package net.minecraft.src:
//            Entity, AxisAlignedBB, EntityLivingBase, MathHelper,
//            World, Vec3D, MovingObjectPosition, NBTTagCompound,
//            EntityPlayer, ItemStack, Item, InventoryPlayer

public class EntityJackball extends Entity
{
	
	public EntityJackball(World world)
	{
		super(world);
		this.xTileSnowball = -1;
		this.yTileSnowball = -1;
		this.zTileSnowball = -1;
		this.inTileSnowball = 0;
		this.inGroundSnowball = false;
		this.shakeSnowball = 0;
		this.ticksInAirSnowball = 0;
		this.setSize(0.25F, 0.25F);
		this.friendly = false;
	}
	
	@Override
	protected void entityInit()
	{
	}
	
	@Override
	public boolean isInRangeToRenderDist(double d)
	{
		double d1 = this.boundingBox.getAverageEdgeLength() * 4D;
		d1 *= 64D;
		return d < d1 * d1;
	}
	
	public EntityJackball(World world, EntityLivingBase entityliving)
	{
		super(world);
		this.xTileSnowball = -1;
		this.yTileSnowball = -1;
		this.zTileSnowball = -1;
		this.inTileSnowball = 0;
		this.inGroundSnowball = false;
		this.shakeSnowball = 0;
		this.ticksInAirSnowball = 0;
		this.thrower = entityliving;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180F * 3.141593F) * 0.16F;
		this.posY -= 0.10000000149011612D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180F * 3.141593F) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		final float f = 0.4F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180F * 3.141593F) * f;
		this.motionZ = MathHelper.cos(this.rotationYaw / 180F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180F * 3.141593F) * f;
		this.motionY = -MathHelper.sin(this.rotationPitch / 180F * 3.141593F) * f;
		this.setJackballHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}
	
	public EntityJackball(World world, double d, double d1, double d2)
	{
		super(world);
		this.xTileSnowball = -1;
		this.yTileSnowball = -1;
		this.zTileSnowball = -1;
		this.inTileSnowball = 0;
		this.inGroundSnowball = false;
		this.shakeSnowball = 0;
		this.ticksInAirSnowball = 0;
		this.ticksInGroundSnowball = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(d, d1, d2);
		this.yOffset = 0.0F;
	}
	
	public void setJackballHeading(double d, double d1, double d2, float f, float f1)
	{
		final float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		d /= f2;
		d1 /= f2;
		d2 /= f2;
		d += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
		d1 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
		d2 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
		d *= f;
		d1 *= f;
		d2 *= f;
		this.motionX = d;
		this.motionY = d1;
		this.motionZ = d2;
		final float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(d, d2) * 180D / 3.1415927410125732D);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(d1, f3) * 180D / 3.1415927410125732D);
		this.ticksInGroundSnowball = 0;
	}
	
	public void setJackballHeading(double d, double d1, double d2, float f, float f1, EntityLivingBase mtarget)
	{
		this.setJackballHeading(d, d1, d2, f, f1);
		this.target = mtarget;
	}
	
	@Override
	public void setVelocity(double d, double d1, double d2)
	{
		this.motionX = d;
		this.motionY = d1;
		this.motionZ = d2;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			final float f = MathHelper.sqrt_double(d * d + d2 * d2);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(d, d2) * 180D / 3.1415927410125732D);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(d1, f) * 180D / 3.1415927410125732D);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i)
	{
		final Entity entity = damagesource.getEntity();
		if (entity != null)
		{
			final double dz = entity.posX - this.posX;
			final double d1z = entity.posZ - this.posZ;
			final double d2z = entity.posY - this.posY - 2D;
			final float f1z = MathHelper.sqrt_double(dz * dz + d1z * d1z) * 0.2F;
			this.setJackballHeading(dz, d2z + f1z + Math.abs(this.motionX / 2D) + Math.abs(this.motionZ / 2D), d1z, 2F, 2F);
			this.motionX *= -5D;
			this.motionZ *= -5D;
			this.tTime = 30;
		}
		return true;
	}
	
	@Override
	public void onUpdate()
	{
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		
		if (this.tTime < 1)
		{
			if (this.target != null)
			{
				final double dz = this.target.posX - this.posX;
				final double d1z = this.target.posZ - this.posZ;
				final double d2z = this.target.posY - this.posY - 2D;
				final float f1z = MathHelper.sqrt_double(dz * dz + d1z * d1z) * 0.4F;
				this.setJackballHeading(dz, d2z - 2D + f1z + Math.abs(this.motionX / 2D) + Math.abs(this.motionZ / 2D), d1z, 0.44F, 28F);
				this.tTime = 12;
				final Random rm = new Random();
				if (!this.target.isEntityAlive() || rm.nextInt(500) == 0)
				{
					this.target = null;
				}
			}
		}
		else
		{
			this.tTime--;
		}
		
		this.motionY = this.motionY * 0.8F + 0.03F;
		
		if (this.shakeSnowball > 0)
		{
			this.shakeSnowball--;
		}
		// TODO
		// if(this.inGroundSnowball)
		// {
		// final Block i = this.worldObj.getBlock(this.xTileSnowball,
		// this.yTileSnowball, this.zTileSnowball);
		// if(i != this.inTileSnowball)
		// {
		// this.inGroundSnowball = false;
		// this.motionX *= this.rand.nextFloat() * 0.2F;
		// this.motionY *= this.rand.nextFloat() * 0.2F;
		// this.motionZ *= this.rand.nextFloat() * 0.2F;
		// this.ticksInGroundSnowball = 0;
		// this.ticksInAirSnowball = 0;
		// } else
		// {
		// this.ticksInGroundSnowball++;
		// if(this.ticksInGroundSnowball == 1200)
		// {
		// this.setDead();
		// }
		// return;
		// }
		// }
		//
		// else
		// {
		// this.ticksInAirSnowball++;
		// }
		
		Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
		vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		
		if (Math.abs(this.motionX) < 0.01F & Math.abs(this.motionZ) < 0.01F)
		{
			this.setDead();
			return;
		}
		else
		{
			this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
		
		if (movingobjectposition != null)
		{
			vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}
		if (!this.worldObj.isRemote)
		{
			Entity entity = null;
			final List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d = 0.0D;
			for (int l = 0; l < list.size(); l++)
			{
				final Entity entity1 = (Entity) list.get(l);
				if (!entity1.canBeCollidedWith() || entity1 == this.thrower && this.ticksInAirSnowball < 5)
				{
					continue;
				}
				final float f4 = 0.3F;
				final AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
				final MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
				if (movingobjectposition1 == null)
				{
					continue;
				}
				final double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
				if (d1 < d || d == 0.0D)
				{
					entity = entity1;
					d = d1;
				}
			}
			
			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}
		if (movingobjectposition != null)
		{
			boolean doeet = true;
			if (movingobjectposition.entityHit != null)
			{
				if (this.friendly)
				{
					if (movingobjectposition.entityHit instanceof EntityLivingBase)
					{
						final EntityLivingBase ell = (EntityLivingBase) movingobjectposition.entityHit;
						// if(ell.spiderfriend) { doeet = false; } //TODO
						if (movingobjectposition.entityHit instanceof EntityPlayer)
						{
							doeet = false;
						}
					}
				}
				else
				{
					if (movingobjectposition.entityHit instanceof EntityJack)
					{
						doeet = false;
					}
				}
				
				if (doeet)
				{
					movingobjectposition.entityHit.motionX += this.motionX * 1D;
					movingobjectposition.entityHit.motionY += this.motionY * 1D;
					movingobjectposition.entityHit.motionZ += this.motionZ * 1D;
					
					if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 6))
						;
				}
			}
			
			if (doeet)
			{
				for (int j = 0; j < 4; j++)
				{
					this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
				}
				
				this.setDead();
			}
		}
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180D / 3.1415927410125732D);
		for (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180D / 3.1415927410125732D); this.rotationPitch - this.prevRotationPitch < -180F; this.prevRotationPitch -= 360F)
		{
		}
		for (; this.rotationPitch - this.prevRotationPitch >= 180F; this.prevRotationPitch += 360F)
		{
		}
		for (; this.rotationYaw - this.prevRotationYaw < -180F; this.prevRotationYaw -= 360F)
		{
		}
		for (; this.rotationYaw - this.prevRotationYaw >= 180F; this.prevRotationYaw += 360F)
		{
		}
		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		final float f2 = 0.03F;
		if (this.isInWater())
		{
			for (int k = 0; k < 4; k++)
			{
				final float f3 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
			}
			
			f1 = 0.8F;
		}
		this.motionX *= f1;
		this.motionY *= f1;
		this.motionZ *= f1;
		this.motionY -= f2;
		this.setPosition(this.posX, this.posY, this.posZ);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setShort("xTile", (short) this.xTileSnowball);
		nbttagcompound.setShort("yTile", (short) this.yTileSnowball);
		nbttagcompound.setShort("zTile", (short) this.zTileSnowball);
		nbttagcompound.setByte("inTile", (byte) this.inTileSnowball);
		nbttagcompound.setByte("shake", (byte) this.shakeSnowball);
		nbttagcompound.setByte("inGround", (byte) (this.inGroundSnowball ? 1 : 0));
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		this.xTileSnowball = nbttagcompound.getShort("xTile");
		this.yTileSnowball = nbttagcompound.getShort("yTile");
		this.zTileSnowball = nbttagcompound.getShort("zTile");
		this.inTileSnowball = nbttagcompound.getByte("inTile") & 0xff;
		this.shakeSnowball = nbttagcompound.getByte("shake") & 0xff;
		this.inGroundSnowball = nbttagcompound.getByte("inGround") == 1;
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityplayer)
	{
		if (this.inGroundSnowball && this.thrower == entityplayer && this.shakeSnowball <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1)))
		{
			// worldObj.playSoundAtEntity(this, "random.pop", 0.2F,
			// ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			// entityplayer.onItemPickup(this, 1);
			this.setDead();
		}
	}
	
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}
	
	private int xTileSnowball;
	private int yTileSnowball;
	private int zTileSnowball;
	private int inTileSnowball;
	private boolean inGroundSnowball;
	public int shakeSnowball;
	private EntityLivingBase thrower;
	private int ticksInGroundSnowball;
	private final int ticksInAirSnowball;
	private EntityLivingBase target;
	private int tTime;
	protected boolean friendly;
}