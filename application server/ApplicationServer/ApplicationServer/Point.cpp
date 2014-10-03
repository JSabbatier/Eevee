#include "Point.h"

Point::Point()
{
}

Point::Point(float x = 0, float y = 0, float z = 0)
{
	a = x;
	b = y;
	c = z;
}

Point::~Point()
{
}

float Point::x()
{
	return a;
}


float Point::y()
{
	return b;
}

float Point::z()
{
	return c;
}

void Point::x(float x)
{
	a = x;
}

void Point::y(float y)
{
	b = y;
}

void Point::z(float z)
{
	c = z;
}