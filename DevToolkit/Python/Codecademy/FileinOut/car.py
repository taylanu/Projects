# Car object with methods built in.
class Car(object):
    condition = "new"
    def __init__(self, model, color, mpg,condition):
        self.model = model
        self.color = color
        self.mpg = mpg
        self.condition = condition
    def display_car(self):
        return "This is a " + self.color + " " + self.model + " with " + str(self.mpg) + " MPG."
    def drive_car(self):
        self.condition = "used"
        

my_car = Car("DeLorean", "silver", 88,"new")
print my_car.condition
my_car.drive_car()
print my_car.condition

class ElectricCar(Car):
    condition = "new"
    def __init__(self,model,color,mpg,battery_type):
        super(ElectricCar, self).__init__(model, color, mpg)
        self.battery_type = battery_type
    def drive_car(self):
        self.condition = "like new"
my_car = ElectricCar("P90D", "titanium", 999,"molten salt")
print my_car.condition
my_car.drive_car()
print my_car.condition