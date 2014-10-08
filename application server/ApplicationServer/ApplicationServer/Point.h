#pragma once
class Point
{
	float a, b, c;
public:
	Point();
	Point(float, float, float);
	~Point();
	float x();
	float y();
	float z();
	void x(float);
	void y(float);
	void z(float); 
	//bool Point::operator==(const Point&, const Point&);
};

